package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebFilter(filterName = "Filter10",urlPatterns = "/*")//对所有资源进行过滤
public class Filter10 implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter 10 begins...");
        HttpServletRequest request = (HttpServletRequest)req;

        String path = request.getRequestURI();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

        Date currentTime = new Date();
        String time = sdf.format(currentTime);
        System.out.println(path + " @ " + time);
        chain.doFilter(req, resp);//执行其他过滤器，若过滤器已执行完毕，则执行请求
        System.out.println("Filter 10 ends.");
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
