package egartech.mreport;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import egartech.servlet.ResponceFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

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

    private ResponceFactory responceFactory = new ResponceFactory();

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
        if (username == null)
        {
            redirectToLogin(req, resp);
            return;
        }
        if (req.getRequestURI().contains("CustomTimed")) {
            responceFactory.formCustomReport(req, resp);
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
}
