package gr.agrora_api;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Order(1)
public class ContactMessageRateLimiter implements Filter {

    // Standard ConcurrentHashMap - no wrapper needed
    private final Map<String, Deque<Long>> requestLog = new ConcurrentHashMap<>();

    private static final long WINDOWS_MS = 60000;
    private static final int MAX_REQUESTS = 3;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        if(request.getRequestURI().equals("api/contact")){
            chain.doFilter(req,res);
            return;
        }

        String ip = request.getRemoteAddr();
        long now = System.currentTimeMillis();

        // 1. Get or Create the Deque for this IP
        // computeIfAbsent is atomic: it guarantees only one Deque is created per IP

        Deque<Long> timestamps = requestLog.computeIfAbsent(ip,k -> new ArrayDeque<>());

        // 2. Lock ONLY this specific user's Deque
        // This prevents race conditions where two requests from the SAME IP
        // try to update the list at the exact same time.
        // Synchronize on the specific deque for this IP to ensure thread safety
        synchronized (timestamps){
            // 1. Remove expired timestamps from the HEAD (O(1)) (oldest)
            while (!timestamps.isEmpty() && (now - timestamps.peekFirst() > WINDOWS_MS)){
                timestamps.pollFirst();
            }

            // 2. Check limit
            if(timestamps.size() >= MAX_REQUESTS){
                ((HttpServletResponse) res).sendError(429,"Too many requests");
                return;
            }

            // 3. Add current timestamp to the TAIL (newest)
            timestamps.offerLast(now);
        }
        chain.doFilter(req,res);

    }

}
