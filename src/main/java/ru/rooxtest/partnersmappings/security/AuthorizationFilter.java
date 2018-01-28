package ru.rooxtest.partnersmappings.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import ru.rooxtest.partnersmappings.domain.Customer;
import ru.rooxtest.partnersmappings.service.PartnersMappingsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Фильтр авторизации по ID
 *
 * Проверяем корректность URL, если некорректен, возвращаем статус NotFound (404)
 *
 * Определяем абонента по ID из заголовка Authorization
 * Если заголовок не задан или абонент с таким ID не найден
 * возвращаем статус Forbidden (403)
 *
 * Проверяем что абонент работает со своими данными.
 * Проверяем только для связок и если не использет @me.
 * Если работает не со своими данными, возвращаем статус Forbidden (403)
 *
 */
@Component
public class AuthorizationFilter extends GenericFilterBean {
    private final static Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);

    private final String AUTH_HEADER = "Authorization";
    private final String URL_CUSTOMERS_FIT_REGEX = "/customers/?";
    private final String URL_CUSTOMER_FIT_REGEX = "/customers/(?:@me|[0-9a-f]{8}\\-(?:[0-9a-f]{4}\\-){3}[0-9a-f]{12})/?";
    private final String URL_PARTNERMAPPINGS_FIT_REGEX = "/customers/(?:@me|[0-9a-f]{8}\\-(?:[0-9a-f]{4}\\-){3}[0-9a-f]{12})/partnermappings/?";
    private final String URL_PARTNERMAPPING_FIT_REGEX = "/customers/(?:@me|[0-9a-f]{8}\\-(?:[0-9a-f]{4}\\-){3}[0-9a-f]{12})/partnermappings/[0-9a-f]{8}\\-(?:[0-9a-f]{4}\\-){3}[0-9a-f]{12}";
    private final String URL_ME_REGEX = "/customers/@me.*";

    private final String AUTH_REGEX = "Bearer[0-9a-f]{8}\\-(?:[0-9a-f]{4}\\-){3}[0-9a-f]{12}";

    @Autowired
    protected AuthorizationHolder authorizationHolder;

    @Autowired
    protected PartnersMappingsService partnersMappingsService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String url = request.getRequestURI();
        log.debug("Request URL: " + url);

        // Проверяем корректность URL
        if (    !url.matches(URL_CUSTOMERS_FIT_REGEX) &&
                !url.matches(URL_CUSTOMER_FIT_REGEX) &&
                !url.matches(URL_PARTNERMAPPINGS_FIT_REGEX) &&
                !url.matches(URL_PARTNERMAPPING_FIT_REGEX))
        {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setStatus(404);
            return;
        }

        String authorizationString = request.getHeader(AUTH_HEADER);
        log.debug("Authorization header: " + authorizationString);
        Customer customer = null;
        if (authorizationString != null && authorizationString.matches(AUTH_REGEX)) {
            UUID customerId = UUID.fromString(authorizationString.substring(6));
            customer = partnersMappingsService.findCustomer(customerId);
            if (customer==null) log.warn("Not found customer for id: " + customerId);
        } else {
            log.warn("Authorization header not found or wrong: " + authorizationString);
        }

        authorizationHolder.setCustomer(customer);

        if (customer == null) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setStatus(403);
            return;
        }

        // проверяем что абонент работает со своими данными
        // проверяем только для связок и если не использем @me
        if ((url.matches(URL_PARTNERMAPPINGS_FIT_REGEX) ||
                url.matches(URL_PARTNERMAPPING_FIT_REGEX)) &&
                !url.matches(URL_ME_REGEX)) {
            String customerIdString = url.substring(11, 47);

            UUID customerId = UUID.fromString(customerIdString);
            if (!customer.getId().equals(customerId)) {
                HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                httpResponse.setStatus(403);
                return;

            }
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }


}
