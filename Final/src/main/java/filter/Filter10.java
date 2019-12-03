package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebFilter(filterName = "Filter10",urlPatterns = "/*")//��������Դ���й���
public class Filter10 implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter 10 begins...");
        HttpServletRequest request = (HttpServletRequest)req;

        String path = request.getRequestURI();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd�� HH:mm");

        Date currentTime = new Date();
        String time = sdf.format(currentTime);
        System.out.println(path + " @ " + time);
        chain.doFilter(req, resp);//ִ������������������������ִ����ϣ���ִ������
        System.out.println("Filter 10 ends.");
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
