package egartech.mreport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.templaterenderer.TemplateRenderer;
import javax.inject.Inject;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.crowd.embedded.api.Group;
/**
 * Created by alander on 15.03.17.
 */
@Scanned
public class EGFeature extends HttpServlet{
//TODO: Release constraints for only authorizede users

    @ComponentImport
    private final UserManager userManager;
    @ComponentImport
    private final LoginUriProvider loginUriProvider;
    @ComponentImport
    private final TemplateRenderer renderer;
    @ComponentImport
    private final GroupManager groupManager;

    @Inject
    public EGFeature(UserManager userManager, LoginUriProvider loginUriProvider, TemplateRenderer renderer, GroupManager groupManager)
    {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.renderer = renderer;
        this.groupManager = groupManager;
    }


    private URI getUri(HttpServletRequest request)
    {
        StringBuffer builder = request.getRequestURL();
        if (request.getQueryString() != null)
        {
            builder.append("?");
            builder.append(request.getQueryString());
        }
        return URI.create(builder.toString());
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.sendRedirect(loginUriProvider.getLoginUri(getUri(request)).toASCIIString());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Entering into custom EGAR JIRA servlet");
        String username = userManager.getRemoteUsername(req);
        if (username == null/* || !userManager.isSystemAdmin(username)*/) {
            redirectToLogin(req, resp);
            return;
        }
        Boolean avalPlugin = false;
        for (Iterator<Group> i = groupManager.getGroupsForUser(username).iterator(); i.hasNext();) {
            Group g = (Group)i.next();
            avalPlugin = (g.getName().compareTo("egarinternal"))==0;
            if (avalPlugin) {
                break;
            }
        }
        if (!avalPlugin) {
            resp.setContentType("text/html;charset=utf-8");
            renderer.render( "accessdenied.vm", resp.getWriter());
        }
        else {
            final Map<String, Object> context = new HashMap<String, Object>();
            context.put("groups", "Your Value");
            resp.setContentType("text/html;charset=utf-8");
            renderer.render("egfeature.vm", context, resp.getWriter());
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

}
