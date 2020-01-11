package github.com.suitelhy.webchat.web;

import github.com.suitelhy.webchat.domain.entity.User;
import github.com.suitelhy.webchat.infrastructure.web.util.*;
import github.com.suitelhy.webchat.application.task.LogTask;
import github.com.suitelhy.webchat.application.task.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 用户信息 - web 交互
 */
/*@Controller*/
@RestController
// @SessionAttributes: Spring Framework Annotation, API Doc
//-> -> <a href="https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/SessionAttributes.html">
//->        SessionAttributes（Spring Framework 5.2.2.RELEASE API）</a>
@SessionAttributes("userid")
public class UserController {

    @Autowired(required = false)
    // 当前会话对应的用户 Entity
    private User user;

    @Resource
    // 用户业务 Task
    private UserTask userTask;

    @Resource
    // 日志记录业务 Task
    private LogTask logTask;
    
//    /**
//     * 视频主页
//     */
//    @RequestMapping(value = "chat")
//    public ModelAndView getVideo(){
//        ModelAndView view = new ModelAndView("mainvideo");
//        return view;
//    }

    /**
     * 聊天主页
     */
    @GetMapping(value = "/chat")
    public ModelAndView getIndex(){
        ModelAndView view = new ModelAndView("index");
        return view;
    }

    /**
     * 用户个人信息 - 显示页面
     */
    @GetMapping(value = "/{userid}")
    public ModelAndView selectUserByUserid(@PathVariable("userid") String userid
            , @ModelAttribute("userid") String sessionid){
        ModelAndView view = new ModelAndView("information");
        user = userTask.selectUserByUserid(userid);
        view.addObject("user", user);
        return view;
    }

    /**
     * 用户个人信息 - 编辑页面
     * @param userid
     * @param sessionid
     * @return
     */
    @RequestMapping(value = "/{userid}/config")
    public ModelAndView setting(@PathVariable("userid") String userid
            , @ModelAttribute("userid") String sessionid) {
        ModelAndView view = new ModelAndView("info-setting");
        user = userTask.selectUserByUserid(userid);
        view.addObject("user", user);
        return view;
    }

    /**
     * 用户信息 - 修改操作
     * @param userid
     * @param sessionid
     * @param user
     * @return
     */
    @PostMapping(value = "/{userid}/update")
    public String update(@PathVariable("userid") String userid
            , @ModelAttribute("userid") String sessionid
            , User user
            , RedirectAttributes attributes
            , NetUtil netUtil
            , LogUtil logUtil
            , CommonDate date
            , WordDefined defined
            , HttpServletRequest request) {
        boolean flag = userTask.update(user);
        if (flag) {
            logTask.insert(logUtil.setLog(userid, date.getTime24()
                    , defined.LOG_TYPE_UPDATE
                    , defined.LOG_DETAIL_UPDATE_PROFILE
                    , netUtil.getIpAddress(request)));
            attributes.addFlashAttribute("message"
                    , "[" + userid + "]资料更新成功!");
        } else {
            attributes.addFlashAttribute("error"
                    , "[" + userid + "]资料更新失败!");
        }
        // 页面重定向 -> 用户个人信息 - 编辑页面
        return "redirect:/{userid}/config";
    }

    /**
     * 密码修改
     * @param userid
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping(value = "/{userid}/pass", method = RequestMethod.POST)
    public String changePassword(@PathVariable("userid") String userid
            , String oldPassword
            , String newPassword
            , RedirectAttributes attributes
            , NetUtil netUtil
            , LogUtil logUtil
            , CommonDate date
            , WordDefined defined
            , HttpServletRequest request) {
        user = userTask.selectUserByUserid(userid);
        if (null != oldPassword
                && oldPassword.equals(user.getPassword())) {
            //--- 密码验证通过
            user.setPassword(newPassword);
            boolean flag = userTask.update(user);
            if (flag) {
                logTask.insert(logUtil.setLog(userid, date.getTime24()
                        , defined.LOG_TYPE_UPDATE
                        , defined.LOG_DETAIL_UPDATE_PASSWORD
                        , netUtil.getIpAddress(request))
                );
                attributes.addFlashAttribute("message"
                        , "[" + userid + "]密码修改成功!");
            } else {
                attributes.addFlashAttribute("error"
                        , "[" + userid + "]密码修改失败!");
            }
        } else {
            //--- 密码验证不通过
            attributes.addFlashAttribute("error", "密码错误!");
        }
        // 页面重定向 -> 用户个人信息 - 编辑页面
        return "redirect:/{userid}/config";
    }

    /**
     * 头像上传
     * @param userid
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/{userid}/upload")
    public String upload(@PathVariable("userid") String userid
            , MultipartFile file
            , HttpServletRequest request
            , UploadUtil uploadUtil
            , RedirectAttributes attributes, NetUtil netUtil, LogUtil logUtil, CommonDate date, WordDefined defined) {
        try{
            String fileUrl = uploadUtil.upload(request, "upload", userid);
            user = userTask.selectUserByUserid(userid);
            user.setProfilehead(fileUrl);
            boolean flag = userTask.update(user);
            if (flag) {
                logTask.insert(logUtil.setLog(userid
                        , date.getTime24()
                        , defined.LOG_TYPE_UPDATE
                        , defined.LOG_DETAIL_UPDATE_PROFILEHEAD
                        , netUtil.getIpAddress(request)));
                attributes.addFlashAttribute("message", "[" + userid + "]头像更新成功!");
            } else {
                attributes.addFlashAttribute("error", "[" + userid + "]头像更新失败!");
            }
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "[" + userid + "]头像更新失败!");
        }
        // 页面重定向 -> 用户个人信息 - 编辑页面
        return "redirect:/{userid}/config";
    }

    /**
     * 获取用户头像
     * @param userid
     */
    @RequestMapping(value = "/{userid}/head")
    public void head(@PathVariable("userid") String userid
            , HttpServletRequest request
            , HttpServletResponse response) {
        user = userTask.selectUserByUserid(userid);
        ServletOutputStream outputStream = null;
        FileInputStream inputStream = null;
        try {
            String path = user.getProfilehead();
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            String picturePath = rootPath + path;
            response.setContentType("image/jpeg; charset=UTF-8");
            outputStream = response.getOutputStream();
            inputStream = new FileInputStream(picturePath);
            byte[] buffer = new byte[1024];
            for (int i; (i = inputStream.read(buffer)) != -1;) {
                outputStream.write(buffer, 0, i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 注意IO流关闭顺序
            if (null != outputStream) {
                try {
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
//    @RequestMapping(value = "registered")
//    public ModelAndView getRegistered(){
//        ModelAndView view = new ModelAndView("registered");
//        return view;
//   }

}
