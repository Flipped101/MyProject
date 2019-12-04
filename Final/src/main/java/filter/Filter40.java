package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

@WebFilter(filterName = "Filter40",urlPatterns = "/*")//��������Դ���й���
public class Filter40 implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        System.out.println("Filter 40 begins...");

        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;

        String path = request.getRequestURI();

        String method = request.getMethod();
        if (!path.contains("/login") && !path.contains("/MyApp")){
            if ("POST-PUT".contains(method)){
                //���������ַ�����ΪUTF-8
                request.setCharacterEncoding("UTF-8");
            }
            //������Ӧ�ַ�����ΪUTF-8
            response.setContentType("text/html;charset=UTF-8");
        }
        chain.doFilter(req, resp);//ִ������������������������ִ����ϣ���ִ������
        System.out.println("Filter 40 ends.");
    }

}
