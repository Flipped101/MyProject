package controller.profTitle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.ProfTitle;
import service.ProfTitleService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * �����з�����֯��һ��Controller(Servlet)��
 */
@WebServlet("/profTitle.ctl")
public class ProfTitleController extends HttpServlet {
    //��ʹ������JSON�������ӹ��ܣ�idΪ�գ�
    //{"description":"idΪnull����ְ��","no":"05","remarks":""}
    //��ʹ������JSON�����޸Ĺ���
    //{"description":"�޸�id=1��ְ��","id":1,"no":"05","remarks":""}

    /**
     * POST, http://49.235.240.61:8080/Flipped/profTitle.ctl, ����ְ��
     * ����һ��ְ�ƶ��󣺽�����ǰ�������JSON�������ӵ����ݿ����
     *
     * @param request  �������
     * @param response ��Ӧ����
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //����request���󣬻�ô��������JSON�ִ�
        String profTitle_json = JSONUtil.getJSON(request);

        //��JSON�ִ�����ΪProfTitle����
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);


        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ��������ProfTitle����
        try {
            ProfTitleService.getInstance().add(profTitleToAdd);
            message.put("message", "���ӳɹ�!");
        } catch (SQLException e) {
            message.put("message", "���ݿ�����쳣!");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "�����쳣!");
            e.printStackTrace();
        }
        //��Ӧmessage��ǰ��
        response.getWriter().println(message);
    }

    /**
     * DELETE, http://49.235.240.61:8080/Flipped/profTitle.ctl?id=1, ɾ��id=1��ְ��
     * ɾ��һ��ְ�ƶ��󣺸�������ǰ�������id��ɾ�����ݿ����id�Ķ�Ӧ��¼
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //��ȡ����id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);


        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();

        //�����ݿ����ɾ����Ӧ��ְ��
        try {
            boolean deleted = ProfTitleService.getInstance().delete(id);
            if (deleted){
                message.put("message", "ɾ���ɹ�!");
            }else {
                message.put("message", "������¼�ѱ�ɾ���򲻴���!");
            }
        } catch (SQLException e) {
            message.put("message", "���ݿ�����쳣!");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "�����쳣!");
            e.printStackTrace();
        }
        //��Ӧmessage��ǰ��
        response.getWriter().println(message);
    }


    /**
     * PUT, http://49.235.240.61:8080/Flipped/profTitle.ctl, �޸�ְ��
     * <p>
     * �޸�һ��ְ�ƶ��󣺽�����ǰ�������JSON���󣬸������ݿ������ͬid�ļ�¼
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String profTitle_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪProfTitle����
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);


        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ���޸�ProfTitle�����Ӧ�ļ�¼
        try {
            ProfTitleService.getInstance().update(profTitleToAdd);
            message.put("message", "�޸ĳɹ�!");
        } catch (SQLException e) {
            message.put("message", "���ݿ�����쳣!");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "�����쳣!");
            e.printStackTrace();
        }
        //��Ӧmessage��ǰ��
        response.getWriter().println(message);
    }

    /**
     * GET, http://49.235.240.61:8080/Flipped/profTitle.ctl?id=1, ��ѯid=1��ְ��
     * GET, http://49.235.240.61:8080/Flipped/profTitle.ctl, ��ѯ���е�ְ��
     * ��һ��������ְ�ƶ�����Ӧ��ǰ��
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //��ȡ����id
        String id_str = request.getParameter("id");

        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        try {
            //���id = null, ��ʾ��Ӧ����ְ�ƶ��󣬷�����Ӧidָ����ְ�ƶ���
            if (id_str == null) {
                responseProfTitles(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseProfTitle(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "���ݿ�����쳣!");
            e.printStackTrace();
            //��Ӧmessage��ǰ��
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "�����쳣!");
            e.printStackTrace();
            //��Ӧmessage��ǰ��
            response.getWriter().println(message);
        }
    }

    //��Ӧһ��ְ�ƶ���
    private void responseProfTitle(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //����id����ְ��
        ProfTitle profTitle = ProfTitleService.getInstance().find(id);
        String profTitle_json = JSON.toJSONString(profTitle);

        //��ӦprofTitle_json��ǰ��
        response.getWriter().println(profTitle_json);
    }

    //��Ӧ����ְ�ƶ���
    private void responseProfTitles(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //�������ְ��
        Collection<ProfTitle> profTitles = ProfTitleService.getInstance().findAll();
        String profTitles_json = JSON.toJSONString(profTitles, SerializerFeature.DisableCircularReferenceDetect);

        //��ӦprofTitles_json��ǰ��
        response.getWriter().println(profTitles_json);
    }
}
