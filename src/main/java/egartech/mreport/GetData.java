package egartech.mreport;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import sun.misc.IOUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

/**
 * Created by alander on 16.03.17.
 */
@Scanned
public class GetData extends HttpServlet {
    @ComponentImport
    private final UserManager userManager;
    @ComponentImport
    private final LoginUriProvider loginUriProvider;
    @ComponentImport
    private final TemplateRenderer renderer;

    @Inject
    public GetData(UserManager userManager, LoginUriProvider loginUriProvider, TemplateRenderer renderer)
    {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.renderer = renderer;
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
        System.out.println("GetDataServlet executing");
        String username = userManager.getRemoteUsername(req);
        if (username == null || !userManager.isSystemAdmin(username))
        {
            redirectToLogin(req, resp);
            return;
        }
        if (req.getRequestURI().contains("CustomTimed")) {
            System.out.println("CustomTimed preparing data");
            resp.setContentType("text/csv");
            resp.setHeader("Content-Disposition", "attachment; filename=\"report.xls\"");
            try
            {
                System.out.println("C111");
                OutputStream outputStream = resp.getOutputStream();
                String outputResult = "xxxx, yyyy, zzzz, aaaa, bbbb, ccccc, dddd, eeee, ffff, gggg\n";
                outputStream.write(outputResult.getBytes());
                outputStream.flush();
                outputStream.close();
                System.out.println("C113");
            }
            catch(Exception e)
            {
                System.out.println(e.toString());
            }
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
}
