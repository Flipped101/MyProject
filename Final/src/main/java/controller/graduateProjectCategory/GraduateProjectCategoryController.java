package controller.graduateProjectCategory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.GraduateProjectCategory;
import service.GraduateProjectCategoryService;
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
 * 将所有方法组织在一个Controller(Servlet)中
 */
@WebServlet("/graduateProjectCategory.ctl")
public class GraduateProjectCategoryController extends HttpServlet {
    //请使用以下JSON测试增加功能（id为空）
    //{"description":"id为null的新毕业设计类别","no":"05","remarks":""}
    //请使用以下JSON测试修改功能
    //{"description":"修改id=1的毕业设计类别","id":1,"no":"05","remarks":""}

    /**
     * POST, http://49.235.240.61:8080/Flipped/graduateProjectCategory.ctl, 增加毕业设计类别
     * 增加一个毕业设计类别对象：将来自前端请求的JSON对象，增加到数据库表中
     *
     * @param request  请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String graduateProjectCategory_json = JSONUtil.getJSON(request);

        //将JSON字串解析为GraduateProjectCategory对象
        GraduateProjectCategory graduateProjectCategoryToAdd = JSON.parseObject(graduateProjectCategory_json, GraduateProjectCategory.class);


        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加GraduateProjectCategory对象
        try {
            GraduateProjectCategoryService.getInstance().add(graduateProjectCategoryToAdd);
            message.put("message", "增加成功!");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常!");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常!");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * DELETE, http://49.235.240.61:8080/Flipped/graduateProjectCategory.ctl?id=1, 删除id=1的毕业设计类别
     * 删除一个毕业设计类别对象：根据来自前端请求的id，删除数据库表中id的对应记录
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);


        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();

        //到数据库表中删除对应的毕业设计类别
        try {
            boolean deleted = GraduateProjectCategoryService.getInstance().delete(id);
            if (deleted){
                message.put("message", "删除成功!");
            }else {
                message.put("message", "此条记录已被删除或不存在!");
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常!");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常!");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }


    /**
     * PUT, http://49.235.240.61:8080/Flipped/graduateProjectCategory.ctl, 修改毕业设计类别
     * <p>
     * 修改一个毕业设计类别对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String graduateProjectCategory_json = JSONUtil.getJSON(request);
        //将JSON字串解析为GraduateProjectCategory对象
        GraduateProjectCategory graduateProjectCategoryToAdd = JSON.parseObject(graduateProjectCategory_json, GraduateProjectCategory.class);


        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改GraduateProjectCategory对象对应的记录
        try {
            GraduateProjectCategoryService.getInstance().update(graduateProjectCategoryToAdd);
            message.put("message", "修改成功!");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常!");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常!");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * GET, http://49.235.240.61:8080/Flipped/graduateProjectCategory.ctl?id=1, 查询id=1的毕业设计类别
     * GET, http://49.235.240.61:8080/Flipped/graduateProjectCategory.ctl, 查询所有的毕业设计类别
     * 把一个或所有毕业设计类别对象响应到前端
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //读取参数id
        String id_str = request.getParameter("id");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有毕业设计类别对象，否则响应id指定的毕业设计类别对象
            if (id_str == null) {
                responseGraduateProjectCategorys(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseGraduateProjectCategory(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常!");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常!");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }
    }

    //响应一个毕业设计类别对象
    private void responseGraduateProjectCategory(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找毕业设计类别
        GraduateProjectCategory graduateProjectCategory = GraduateProjectCategoryService.getInstance().find(id);
        String graduateProjectCategory_json = JSON.toJSONString(graduateProjectCategory);

        //响应graduateProjectCategory_json到前端
        response.getWriter().println(graduateProjectCategory_json);
    }

    //响应所有毕业设计类别对象
    private void responseGraduateProjectCategorys(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有毕业设计类别
        Collection<GraduateProjectCategory> graduateProjectCategorys = GraduateProjectCategoryService.getInstance().findAll();
        String graduateProjectCategorys_json = JSON.toJSONString(graduateProjectCategorys, SerializerFeature.DisableCircularReferenceDetect);

        //响应graduateProjectCategorys_json到前端
        response.getWriter().println(graduateProjectCategorys_json);
    }
}
