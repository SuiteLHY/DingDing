package github.com.suitelhy.dingding.core.infrastructure.web.vo;

import github.com.suitelhy.dingding.core.infrastructure.config.springdata.attribute.converter.VoAttributeConverter;
import github.com.suitelhy.dingding.core.infrastructure.domain.model.VoModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * HTTP 相关 <- VO
 *
 * @param <VO>
 * @param <V>
 * @param <_DESCRIPTION>
 * @Description 目前主要是明确项目中对 HTTP 相关规范的选择和定义.
 */
public interface HTTP<VO extends Enum<VO> & VoModel<VO, V, _DESCRIPTION>, V extends Number, _DESCRIPTION>
        extends VoModel<VO, V, _DESCRIPTION> {

    /**
     * HTTP Method
     *
     * @see <a href="https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Methods">HTTP 请求方法 - HTTP | MDN</a>
     */
    enum MethodVo
            implements VoModel<MethodVo, Integer, String> {
        GET(1
                , HttpMethod.GET.name()
                , "获取所请求的资源。保证幂等性。"
                , HttpMethod.GET),
        HEAD(2
                , HttpMethod.HEAD.name()
                , "获取所请求的资源（仅消息头部）。保证幂等性。"
                .concat("通常用于：")
                .concat("（1）确认资源是否存在；")
                .concat("（2）确认资源是否被修改；")
                .concat("（3）服务器端必须确保 HEAD 请求返回的消息与 GET 请求返回的消息的头部完全一致。")
                , HttpMethod.HEAD),
        POST(3
                , HttpMethod.POST.name()
                , "修改资源（更新已有的或添加新的资源）。"
                , HttpMethod.POST),
        PUT(4
                , HttpMethod.PUT.name()
                , "用请求所提供的数据替换目标资源的所有当前表示（全量更新）。保证幂等性。"
                , HttpMethod.PUT),
        PATCH(5
                , HttpMethod.PATCH.name()
                , "对目标资源做部分修改（增量更新）。保证幂等性。相比于 PUT 请求，通常占用更少的带宽。"
                , HttpMethod.PATCH),
        DELETE(6
                , HttpMethod.DELETE.name()
                , "删除所请求的资源。保证幂等性。"
                , HttpMethod.DELETE),
        CONNECT(7
                , "CONNECT"
                , "用于打开与目标资源的双向通信; 它可以用来打开通信隧道。"
                .concat("\n例如, 该 CONNECT 方法可用于访问使用 SSL（HTTPS）的网站。客户端要求HTTP代理服务器将TCP连接隧穿到所需的目的地。")
                .concat("\n然后, 服务器继续代表客户端进行连接。服务器建立连接后, 代理服务器将继续代理与客户端之间的TCP流。")
                , null),
        OPTIONS(8
                , HttpMethod.OPTIONS.name()
                , "获取目标资源或整个服务器的资源的通讯选项（可用的 HTTP Methods）。保证幂等性。"
                , HttpMethod.OPTIONS),
        TRACE(9
                , HttpMethod.TRACE.name()
                , "获取服务器端收到的请求 -> 沿着[从客户端到目标资源的路径]执行一个消息环回测试。主要用于测试和诊断。"
                , HttpMethod.TRACE);

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<MethodVo, Integer, String> {

            // (单例模式 - 登记式)
            private static class Factory {
                private static final Converter SINGLETON = new Converter();
            }

            private Converter() {
                super(MethodVo.class);
            }

            @NotNull
            public static Converter getInstance() {
                return Converter.Factory.SINGLETON;
            }

        }

        public final int code;

        @NotNull
        public final String name;

        @NotNull
        public final String description;

        @Nullable
        public final HttpMethod httpMethod;

        MethodVo(int code
                , @NotNull String name
                , @NotNull String description
                , @Nullable HttpMethod httpMethod)
                throws IllegalArgumentException {
            if (null == name) {
                throw new IllegalArgumentException("非法输入: <param>name</param>");
            }
            if (null == description) {
                throw new IllegalArgumentException("非法输入: <param>description</param>");
            }

            this.code = code;
            this.name = name;
            this.description = description;
            this.httpMethod = httpMethod;
        }

        /**
         * VO 的值
         *
         * @return {@link Integer}  可为 <code>null</code>
         * @Description Unique attribute.
         */
        @Override
        public Integer value() {
            return this.code;
        }

        /**
         * VO 的详细信息
         *
         * @return {@link String}
         */
        @Override
        public String description() {
            return this.description;
        }

        /**
         * VO 的 (展示)名称
         *
         * @return {@link String}
         */
        @NotNull
        @Override
        public String displayName() {
            return this.name;
        }

        @Override
        public String toString() {
            return VoModel.toString(this);
        }

        /**
         * 提供类型转换器
         *
         * @return {@link Converter}
         * @Design 为持久化类型转换功能提供支持.
         */
        @NotNull
        @Override
        @SuppressWarnings("unchecked")
        public Converter voAttributeConverter() {
            return Converter.getInstance();
        }

        /**
         * 等效比较
         *
         * @param name HTTP Method name
         * @return 比较结果
         */
        public boolean equals(String name) {
            return this.name().equals(name);
        }

    }

    /**
     * HTTP Method
     *
     * @see <a href="https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Methods">HTTP 请求方法 - HTTP | MDN</a>
     * @see <a href="https://zh.wikipedia.org/wiki/HTTP%E7%8A%B6%E6%80%81%E7%A0%81">HTTP状态码 - 维基百科，自由的百科全书</a>
     */
    enum StatusVo
            implements VoModel<StatusVo, Integer, String> {
        //----- 1xx Informational -----//

        CONTINUE(100
                , "Continue"
                , "服务器已经接收到请求头，并且客户端应该继续发送请求主体（在需要发送请求主体的情况下：例如，POST请求）；如果请求已经完成，则忽略这个响应。"
                .concat("\n服务器必须在请求完成后向客户端发送一个最终响应。要使服务器检查请求的头部，客户端必须在其初始请求中发送 <code>Expect: 100-continue</code> 作为头部，并在发送正文之前接收 <code>100 Continue</code> 状态代码。")
                .concat("\n响应代码 <code>417</code> 期望失败表示请求不应继续。")
                , HttpStatus.CONTINUE
                , HttpStatus.Series.valueOf(HttpStatus.CONTINUE).name()), SWITCHING_PROTOCOLS(101
                , "Switching Protocols"
                , "服务器已经理解了客户端的请求，并将通过消息头中的 Upgrade 字段通知客户端采用（不同于当前的）指定的协议来完成这个请求。"
                .concat("\n在发送完这个响应最后的空行后，服务器将会切换到在消息头 Upgrade 字段中定义的那些协议。")
                .concat("\n只有在切换新的协议更有好处的时候（即升级协议的情况），才应该采取类似措施。例如，切换到新的 HTTP 版本（如 HTTP/2）比旧版本更有优势，或者切换到一个实时且同步的协议（如 WebSocket）以传输需要此类特性的资源。")
                , HttpStatus.SWITCHING_PROTOCOLS
                , HttpStatus.Series.valueOf(HttpStatus.SWITCHING_PROTOCOLS).name()), PROCESSING(102
                , "Processing"
                , "【WebDAV; RFC 2518】用于 WebDAV 请求（WebDAV 请求可能包含许多涉及文件操作的子请求，需要很长时间才能完成请求）。"
                .concat("\n该状态码表示[服务器已经收到并正在处理请求，但无响应可用]。")
                .concat("\n这样可以防止[客户端超时，并假设请求丢失]。")
                , HttpStatus.PROCESSING
                , HttpStatus.Series.valueOf(HttpStatus.PROCESSING).name()), EARLY_HINTS(103
                , "Early Hints "
                , "【RFC 8297】用来在最终的 HTTP 消息之前返回一些响应头。"
                .concat("\n此状态码主要用于与 Link 链接头一起使用，以允许用户代理在服务器仍在准备响应时开始预加载资源。")
                , null
                , HttpStatus.Series.valueOf(HttpStatus.CONTINUE).name())

        //----- 2xx Success -----//

        , OK(200
                , "OK"
                , "请求已成功，请求所希望的响应头或数据体将随此响应返回。"
                .concat("\n实际的响应将取决于所使用的请求方法。例如：")
                .concat("\n· GET：资源已被提取并在消息正文中传输。")
                .concat("\n· HEAD：实体标头位于消息正文中。")
                .concat("\n· POST：描述动作结果的资源在消息体中传输。")
                .concat("\n· TRACE：消息正文包含服务器收到的请求消息。")
                , HttpStatus.OK
                , HttpStatus.Series.valueOf(HttpStatus.OK).name()), CREATED(201
                , "Created"
                , "该请求已成功，并因此创建了新的资源；[新创建的目标资源]的 URI 已经随 Location 头信息返回。"
                .concat("\n在需要的资源无法及时创建的情况下，应当返回 <code>202 Accepted</code>。")
                .concat("\n这通常是在 POST 请求，或是某些 PUT 请求之后返回的响应。")
                , HttpStatus.CREATED
                , HttpStatus.Series.valueOf(HttpStatus.CREATED).name()), ACCEPTED(202
                , "Accepted"
                , "服务器已接受请求，但尚未处理完成（未响应结果）。"
                .concat("\n该状态码意味着不会有一个异步的响应去表明[当前请求的结果]/[预期另外的进程和服务去处理请求，或者批处理]。")
                .concat("\n最终该请求可能会也可能不会被执行，并且可能在处理发生时被禁止。")
                , HttpStatus.ACCEPTED
                , HttpStatus.Series.valueOf(HttpStatus.ACCEPTED).name()), NON_AUTHORITATIVE_INFORMATION(203
                , "Non-Authoritative Information"
                , "【自 HTTP / 1.1 起】服务器已成功处理了请求，但返回的实体头部元信息不是在原始服务器上有效的确定集合，而是来自本地或者第三方的拷贝。当前的信息可能是原始版本的子集或者超集。"
                .concat("\n例如，包含资源的元数据可能导致原始服务器知道元信息的超集。")
                .concat("\n使用此状态码不是必须的，而且只有在响应不使用此状态码便会返回<code>200 OK</code>的情况下才适用。")
                .concat("\n多数情况下，该服务器是一个转换代理服务器（transforming proxy，例如：网络加速器）。")
                , HttpStatus.NON_AUTHORITATIVE_INFORMATION
                , HttpStatus.Series.valueOf(HttpStatus.NON_AUTHORITATIVE_INFORMATION).name()), NO_CONTENT(204
                , "No Content"
                , "服务器成功处理了请求，没有返回任何内容（不需要返回任何实体内容，并且希望返回更新了的元信息）。"
                .concat("\n响应可能通过实体头部的形式，返回新的或更新后的元信息。如果存在这些头部信息，则应当与所请求的变量相呼应。")
                .concat("\n如果客户端是浏览器的话，那么用户浏览器应保留发送了该请求的页面，而不产生任何文档视图上的变化，即使按照规范新的或更新后的元信息应当被应用到用户浏览器活动视图中的文档。")
                .concat("\n由于 <code>204</code> 响应被禁止包含任何消息体，因此它始终以消息头后的第一个空行结尾。")
                .concat("\n常见应用场景：")
                .concat("\n· 在强制门户功能中，Wifi设备连接到需要进行Web认证的Wifi接入点时，通过访问一个能生成HTTP 204响应的的网站，如果能正常收到204响应，则代表无需Web认证，否则会弹出网页浏览器界面，显示出Web网页认证界面用于让用户认证登录。")
                .concat("\n· 在未更新网页的情况下，可确保浏览器继续显示当前文档。")
                , HttpStatus.NO_CONTENT
                , HttpStatus.Series.valueOf(HttpStatus.NO_CONTENT).name()), RESET_CONTENT(205
                , "Reset Content"
                , "服务器成功处理了请求，但没有返回任何内容。与204响应不同，此响应要求请求者重置文档视图。"
                .concat("\n该响应主要是被用于接受用户输入后，立即重置表单，以便用户能够轻松地开始另一次输入。")
                .concat("\n与 <code>204</code> 响应一样，该响应也被禁止包含任何消息体，且以消息头后的第一个空行结束。")
                , HttpStatus.RESET_CONTENT
                , HttpStatus.Series.valueOf(HttpStatus.RESET_CONTENT).name()), PARTIAL_CONTENT(206
                , "Partial Content"
                , "服务器已经成功处理了部分 GET 请求。"
                .concat("\n该请求必须包含 Range 头信息来指示客户端希望得到的内容范围，并且可能包含 If-Range 来作为请求条件。")
                .concat("\n常见应用场景：")
                .concat("\n类似于 FlashGet 或者迅雷这类的 HTTP 下载工具都是使用此类响应实现[断点续传]或者[将一个大文档分解为多个下载段同时下载]。")
                , HttpStatus.PARTIAL_CONTENT
                , HttpStatus.Series.valueOf(HttpStatus.PARTIAL_CONTENT).name()), MULTI_STATUS(207
                , "Multi-Status"
                , "【WebDAV; RFC 4918】代表之后的消息体将是一个 XML 消息，并且[可能依照之前子请求数量的不同，包含一系列独立的响应代码]。"
                , HttpStatus.MULTI_STATUS
                , HttpStatus.Series.valueOf(HttpStatus.MULTI_STATUS).name()), ALREADY_REPORTED(208
                , "Already Reported"
                , "【WebDAV; RFC 5842】<code>propstat</code> 响应元素中声明的成员，已经在[（多状态）响应之前的部分]被列举。用于避免[重复枚举多个绑定的内部成员到同一个集合]。"
                , HttpStatus.ALREADY_REPORTED
                , HttpStatus.Series.valueOf(HttpStatus.ALREADY_REPORTED).name()), IM_USED(226
                , "IM Used"
                , "【HTTP Delta encoding; RFC 3229】服务器已经完成了对资源的 GET 请求，并且该响应是对当前实例应用的[一个或多个实例操作结果]的表示。"
                , HttpStatus.IM_USED
                , HttpStatus.Series.valueOf(HttpStatus.IM_USED).name())

        //----- 3xx Redirection -----//

        , MULTIPLE_CHOICES(300
                , "Multiple Choices"
                , "被请求的资源有一系列可供选择的回馈信息，每个都有自己特定的地址和浏览器驱动的商议信息。用户或浏览器能够自行选择一个首选的地址进行重定向。"
                .concat("\n除非这是一个 HEAD 请求，否则该响应应当包括一个资源特性及地址的列表的实体，以便用户或浏览器从中选择最合适的重定向地址。这个实体的格式由 <code>Content-Type</code> 定义的格式所决定。")
                .concat("\n浏览器可能根据响应的格式以及浏览器自身能力，自动作出最合适的选择（<code>RFC 2616</code> 规范并没有规定这样的自动选择该如何进行）。")
                .concat("\n如果服务器本身已经有了首选的回馈选择，那么在 <code>Location</code> 中应当指明这个回馈的 URI；浏览器可能会将这个 <code>Location</code> 值作为自动重定向的地址。此外，除非额外指定，否则这个响应也是可缓存的。")
                , HttpStatus.MULTIPLE_CHOICES
                , HttpStatus.Series.valueOf(HttpStatus.MULTIPLE_CHOICES).name()), MOVED_PERMANENTLY(301
                , "Moved Permanently"
                , "被请求的资源已永久移动到新位置，并且将来任何对此资源的引用都应该使用本响应返回的若干个 URI 之一。"
                .concat("\n如果可能，拥有链接编辑功能的客户端应当自动把请求的地址修改为从服务器反馈回来的地址。除非额外指定，否则这个响应也是可缓存的。")
                .concat("\n新的永久性的 URI 应当在响应的 <code>Location</code> 域中返回。除非这是一个 HEAD 请求，否则响应的实体中应当包含指向新的 URI 的超链接及其简要说明。")
                .concat("\n如果这不是一个 GET 或者 HEAD 请求，那么浏览器应该禁止自动进行重定向（除非得到用户的确认，因为请求的条件可能因此发生变化）。")
                .concat("\n【注意】对于某些使用 HTTP/1.0 协议的浏览器，当它们发送的 POST 请求得到了一个 <code>301</code> 响应的话，接下来的重定向请求将会变成 GET 方式。")
                , HttpStatus.MOVED_PERMANENTLY
                , HttpStatus.Series.valueOf(HttpStatus.MOVED_PERMANENTLY).name()), FOUND(302
                , "Found"
                , "要求客户端执行临时重定向（原始描述短语为“Moved Temporarily”）。由于这样的重定向是临时的，客户端应当继续向原有地址发送之后的请求。"
                .concat("\n新的临时性的 URI 应当在响应的 <code>Location</code> 域中返回。除非这是一个 HEAD 请求，否则响应的实体中应当包含指向新的 URI 的超链接及其简要说明。")
                .concat("\n如果这不是一个 GET 或者 HEAD 请求，那么浏览器应该禁止自动进行重定向（除非得到用户的确认，因为请求的条件可能因此发生变化）。")
                .concat("\n【注意】虽然 <code>RFC 1945</code> 和 <code>RFC 2068</code> 规范不允许客户端在重定向时改变请求的方法，但是很多现存的浏览器将 <code>302</code> 响应视作为 <code>303</code> 响应，并且使用 GET 方式访问在 <code>Location</code> 中规定的 URI，而无视原先请求的方法。")
                .concat("因此状态码 <code>303</code> 和 <code>307</code> 被添加了进来，用以明确服务器期待客户端进行何种反应。")
                , HttpStatus.FOUND
                , HttpStatus.Series.valueOf(HttpStatus.FOUND).name()), SEE_OTHER(303
                , "See Other"
                , "对应当前请求的响应可以在另一个 URI 上被找到，而且客户端应当采用 GET 的方式访问那个资源。"
                .concat("\n这个方法的存在主要是为了允许由脚本激活的POST请求输出重定向到一个新的资源。")
                .concat("\n新的 URI 应当在响应的 <code>Location</code> 域中返回。除非这是一个 HEAD 请求，否则响应的实体中应当包含指向新的 URI 的超链接及其简要说明。")
                .concat("\n<code>303</code> 响应禁止被缓存。当然，第二个请求（重定向）可能被缓存。")
                .concat("\n【注意】许多 HTTP/1.1 版本以前的浏览器不能正确理解 <code>303</code> 状态。")
                .concat("如果需要考虑与这些浏览器之间的互动，<code>302</code> 状态码应该可以胜任，因为大多数的浏览器处理 <code>302</code> 响应时的方式恰恰就是上述规范要求客户端处理 <code>303</code> 响应时应当做的。")
                , HttpStatus.SEE_OTHER
                , HttpStatus.Series.valueOf(HttpStatus.SEE_OTHER).name()), NOT_MODIFIED(304
                , "Not Modified"
                , "表示资源在由请求头中的 <code>If-Modified-Since</code> 或 <code>If-None-Match</code> 参数指定的这一版本之后，未曾被修改。在这种情况下，由于客户端仍然具有以前下载的副本，因此不需要重新传输资源。"
                .concat("\n如果客户端发送了一个带条件的 GET 请求且该请求已被允许，而文档的内容（自上次访问以来或者根据请求的条件）并没有改变，则服务器应当返回这个状态码。")
                .concat("\n<code>304</code> 响应禁止包含消息体，因此始终以消息头后的第一个空行结尾。")
                , HttpStatus.NOT_MODIFIED
                , HttpStatus.Series.valueOf(HttpStatus.NOT_MODIFIED).name()), @Deprecated USE_PROXY(305
                , "Use Proxy"
                , "【This deprecated API should not longer be used, but will probably still work.】"
                .concat("\n被请求的资源必须通过指定的代理才能被访问。<code>Location</code> 域中将给出指定的代理所在的 URI 信息，接收者需要重复发送一个单独的请求，通过这个代理才能访问相应资源。")
                .concat("\n只有原始服务器才能创建 <code>305</code> 响应。")
                .concat("\n许多 HTTP 客户端（像是 Mozilla 和 Internet Explorer）都没有正确处理这种状态码的响应，主要是出于安全考虑。")
                .concat("\n【注意】<code>RFC 2068</code> 中没有明确[<code>305</code> 响应是为了重定向一个单独的请求，而且只能被原始服务器创建]。忽视这些限制可能导致严重的安全后果。")
                , HttpStatus.USE_PROXY
                , HttpStatus.Series.valueOf(HttpStatus.USE_PROXY).name()), @Deprecated SWITCH_PROXY(306
                , "Switch Proxy"
                , "【API 已淘汰】在最新版的规范中，该状态码（<code>306</code>）已经不再被使用。"
                .concat("\n最初是指“后续请求应使用指定的代理”。")
                , null
                , HttpStatus.Series.valueOf(HttpStatus.MULTIPLE_CHOICES).name()), TEMPORARY_REDIRECT(307
                , "Temporary Redirect"
                , "请求的资源现在临时从其他（不同的）URI 响应请求。由于这样的重定向是临时的，客户端应当继续向原有地址发送之后的请求。"
                .concat("\n只有在 <code>Cache-Control</code> 或 <code>Expires</code> 中进行了指定的情况下，这个响应才是可缓存的。")
                .concat("\n与 <code>302</code> 状态码相反 —— 当重新发出原始请求时，不允许更改请求方法。")
                .concat("例如，应该使用另一个 POST 请求来重复 POST 请求。")
                , HttpStatus.TEMPORARY_REDIRECT
                , HttpStatus.Series.valueOf(HttpStatus.TEMPORARY_REDIRECT).name()), PERMANENT_REDIRECT(308
                , "Permanent Redirect"
                , "【RFC 7538】请求和所有将来的请求应该使用另一个 URI。所请求的资源现在永久位于[由 <code>Location</code> HTTP Response 标头指定的另一个 URI]上。"
                .concat("\n该状态码与 <code>301 Moved Permanently HTTP</code> 响应代码具有相同的语义，但用户代理不能更改所使用的 HTTP 方法 —— [如果在第一个请求中使用 POST，则必须在第二个请求中使用 POST]。")
                , HttpStatus.PERMANENT_REDIRECT
                , HttpStatus.Series.valueOf(HttpStatus.PERMANENT_REDIRECT).name())

        //----- 4xx Client Error -----//

        , BAD_REQUEST(400
                , "Bad Request"
                , "由于明显的客户端错误（语义有误或请求参数有误。例如，格式错误的请求语法、请求大小过大、无效的请求消息或欺骗性路由请求），服务器不能或不会处理该请求。除非进行修改，否则客户端不应该重复提交这个请求。"
                , HttpStatus.BAD_REQUEST
                , HttpStatus.Series.valueOf(HttpStatus.BAD_REQUEST).name()), UNAUTHORIZED(401
                , "Unauthorized"
                , "【RFC 7235】未认证 —— 当前请求需要用户验证，但是用户没有提供必要的凭据。"
                .concat("\n该响应必须包含一个适用于被请求资源的 <code>WWW-Authenticate</code> 信息头用以询问用户信息。")
                .concat("\n客户端可以重复提交一个包含恰当的 Authorization 头信息的请求。")
                .concat("\n如果当前请求已经包含了 Authorization 证书，那么该响应（<code>401</code>）代表着服务器验证已经拒绝了这些证书。")
                .concat("\n如果该响应（<code>401</code>）包含了与前一个响应相同的身份验证询问，且浏览器已经至少尝试了一次验证，那么浏览器应当向用户展示响应中包含的实体信息，因为这个实体信息中可能包含了相关诊断信息。")
                .concat("\n【注意】当网站（通常是网站域名）禁止 IP 地址时，有些网站状态码显示的 <code>401</code>，表示该特定地址被拒绝访问网站。")
                .concat("\n常见应用场景：HTTP 基本认证、HTTP 摘要认证。")
                , HttpStatus.UNAUTHORIZED
                , HttpStatus.Series.valueOf(HttpStatus.UNAUTHORIZED).name()), PAYMENT_REQUIRED(402
                , "Payment Required"
                , "【预留，将来可能使用】此响应码的最初目的是用于数字支付系统，然而现在并未使用（几乎没有服务商使用该状态码，且该状态码通常不被按照最初设计目的来使用）。"
                .concat("\n应用场景：")
                .concat("\nGoogle Developers API 使用此状态码 —— 来自特定开发人员的请求已超过每日限制。")
                , HttpStatus.PAYMENT_REQUIRED
                , HttpStatus.Series.valueOf(HttpStatus.PAYMENT_REQUIRED).name()), FORBIDDEN(403
                , "Forbidden"
                , "服务器已经理解请求，但是拒绝执行它。"
                .concat("\n与 <code>401</code> 响应不同的是，身份验证并不能提供任何帮助，而且这个请求也不应该被重复提交。")
                .concat("\n如果这不是一个 HEAD 请求，而且服务器希望能够讲清楚为何请求不能被执行，那么就应该在实体内描述拒绝的原因。")
                .concat("当然服务器也可以返回一个 <code>404</code> 响应，假如它不希望让客户端获得任何信息。")
                , HttpStatus.FORBIDDEN
                , HttpStatus.Series.valueOf(HttpStatus.FORBIDDEN).name()), NOT_FOUND(404
                , "Not Found"
                , "请求失败，请求所希望得到的资源未被在服务器上发现，但允许用户的后续请求。"
                .concat("\n没有信息能够告诉用户这个状况到底是暂时的还是永久的。")
                .concat("\n假如服务器知道情况的话，应当使用 <code>410</code> 状态码来告知旧资源因为某些内部的配置机制问题，已经永久的不可用，而且没有任何可以跳转的地址。")
                .concat("\n应用场景：")
                .concat("\n<code>404</code> 这个状态码被广泛应用于当服务器不想揭示到底为何请求被拒绝或者没有其他适合的响应可用的情况下。")
                , HttpStatus.NOT_FOUND
                , HttpStatus.Series.valueOf(HttpStatus.NOT_FOUND).name()), METHOD_NOT_ALLOWED(405
                , "Method Not Allowed"
                , "请求方法不能被用于所请求的资源。"
                .concat("\n该响应必须返回一个 <code>Allow</code> 头信息用以表示出当前资源能够接受的请求方法的列表。")
                .concat("例如，需要通过 POST 呈现数据的表单上的 GET 请求，或只读资源上的 PUT 请求。")
                .concat("\n鉴于 PUT，DELETE 方法会对服务器上的资源进行写操作，因而绝大部分的网页服务器都不支持或者在默认配置下不允许上述请求方法，对于此类请求均会返回 <code>405</code> 错误。")
                , HttpStatus.METHOD_NOT_ALLOWED
                , HttpStatus.Series.valueOf(HttpStatus.METHOD_NOT_ALLOWED).name()), NOT_ACCEPTABLE(406
                , "Not Acceptable"
                , "请求的资源的内容特性无法满足请求头中的条件，因而无法生成响应实体，该请求不可接受。"
                .concat("\n除非这是一个 HEAD 请求，否则该响应就应当返回一个包含可以让用户或者浏览器从中选择最合适的实体特性以及地址栏表的实体。")
                .concat("实体的格式由 <code>Content-Type</code> 头中定义的媒体类型决定。")
                .concat("\n浏览器可以根据格式及自身能力自行作出最佳选择。但是，规范中并没有定义任何作出此类自动选择的标准。")
                .concat("\n【参见】<a href=\"https://zh.wikipedia.org/wiki/%E5%86%85%E5%AE%B9%E5%8D%8F%E5%95%86\">内容协商</a>")
                , HttpStatus.NOT_ACCEPTABLE
                , HttpStatus.Series.valueOf(HttpStatus.NOT_ACCEPTABLE).name()), PROXY_AUTHENTICATION_REQUIRED(407
                , "Proxy Authentication Required"
                , "【RFC 2617】用户没有提供必要的凭据，与 <code>401</code> 响应类似，只不过请求者应当使用代理进行授权（即客户端必须在代理服务器上进行身份验证）。"
                .concat("\n代理服务器必须返回一个 <code>Proxy-Authenticate</code> 用以进行身份询问。")
                .concat("\n客户端可以返回一个 <code>Proxy-Authorization</code> 信息头用以验证。")
                , HttpStatus.PROXY_AUTHENTICATION_REQUIRED
                , HttpStatus.Series.valueOf(HttpStatus.PROXY_AUTHENTICATION_REQUIRED).name()), REQUEST_TIMEOUT(408
                , "Request Timeout"
                , "请求超时。"
                .concat("\n客户端没有在服务器预备等待的时间内完成一个请求的发送。")
                .concat("\n客户端可以随时再次提交这一请求而无需进行任何更改。")
                , HttpStatus.REQUEST_TIMEOUT
                , HttpStatus.Series.valueOf(HttpStatus.REQUEST_TIMEOUT).name()), CONFLICT(409
                , "Conflict"
                , "表示因为请求存在冲突而无法处理该请求"
                .concat("\n这个代码只允许用在这样的情况下才能被使用：用户被认为能够解决冲突，并且会重新提交新的请求。")
                .concat("\n该响应应当包含足够的信息以便用户发现冲突的源头。")
                .concat("\n常见应用场景：")
                .concat("多个同步更新之间的编辑冲突。")
                , HttpStatus.CONFLICT
                , HttpStatus.Series.valueOf(HttpStatus.CONFLICT).name()), GONE(410
                , "Gone"
                , "所请求的资源在服务器上已经不再可用，而且没有任何已知的转发地址。这样的状况应当被认为是永久性的。"
                .concat("\n在收到该状态码（<code>410 Gone</code>）后，用户应停止请求该资源。")
                .concat("如果可能，拥有链接编辑功能的客户端应当在获得用户许可后删除所有指向该请求地址的引用。")
                .concat("\n除非额外说明，否则这个响应是可缓存的。")
                .concat("\n如果服务器不知道或者无法确定这个状况是否是永久的，那么就应该使用 <code>404</code> 状态码。")
                .concat("\n大多数服务端不会使用此状态码，而是直接使用 <code>404</code> 状态码。")
                , HttpStatus.GONE
                , HttpStatus.Series.valueOf(HttpStatus.GONE).name()), LENGTH_REQUIRED(411
                , "Length Required"
                , "服务器拒绝在没有定义 Content-Length 头的情况下接受请求。"
                .concat("\n在添加了表明请求消息体长度的有效 Content-Length 头之后，客户端可以再次提交该请求。")
                , HttpStatus.LENGTH_REQUIRED
                , HttpStatus.Series.valueOf(HttpStatus.LENGTH_REQUIRED).name()), PRECONDITION_FAILED(412
                , "Precondition Failed"
                , "【RFC 7232】客户端请求信息的先决条件错误（服务器在验证在请求的头字段中给出先决条件时，没能满足其中的一个或多个）。"
                .concat("\n这个状态码允许客户端在获取资源时在请求的元信息（请求头字段数据）中设置先决条件，以此避免该请求方法被应用到其希望的内容以外的资源上。")
                , HttpStatus.PRECONDITION_FAILED
                , HttpStatus.Series.valueOf(HttpStatus.PRECONDITION_FAILED).name()), PAYLOAD_TOO_LARGE(413
                , "Payload Too Large"
                , "【RFC 7231】服务器拒绝处理当前请求，因为该请求提交的实体数据大小超过了服务器愿意或者能够处理的范围。"
                .concat("\n此种情况下，服务器可以关闭连接以免客户端继续发送此请求。")
                .concat("\n如果这个状况是临时的，服务器应当返回一个 <code>Retry-After</code> 的响应头，以告知客户端可以在多少时间以后重新尝试。")
                , HttpStatus.PAYLOAD_TOO_LARGE
                , HttpStatus.Series.valueOf(HttpStatus.PAYLOAD_TOO_LARGE).name()), URI_TOO_LONG(414
                , "URI Too Long"
                , "【RFC 7231】请求的 URI 长度超过了服务器能够解释的长度，因此服务器拒绝对该请求提供服务。"
                .concat("\n这比较少见，常见的情况包括：")
                .concat("\n-> · 本应使用 POST 方法的表单提交变成了 GET 方法，导致查询字符串过长。")
                .concat("\n-> · 重定向 URI “黑洞”，例如每次重定向把旧的 URI 作为新的 URI 的一部分，导致在若干次重定向后 URI 超长。")
                .concat("\n-> · 客户端正在尝试利用某些服务器中存在的安全漏洞攻击服务器。")
                .concat("这类服务器使用固定长度的缓冲读取或操作请求的 URI，当 GET 后的参数超过边界值后，可能会产生缓冲区溢出，导致任意代码被执行。没有此类漏洞的服务器，应当返回 <code>414</code> 状态码。")
                , HttpStatus.URI_TOO_LONG
                , HttpStatus.Series.valueOf(HttpStatus.URI_TOO_LONG).name()), UNSUPPORTED_MEDIA_TYPE(415
                , "Unsupported Media Type"
                , "对于当前请求的方法和所请求的资源，请求中提交的互联网媒体类型不是服务器所支持的格式，因此请求被拒绝。"
                .concat("\n例如，客户端将图像上传格式为 svg，但服务器要求图像使用上传格式为 jpg。")
                , HttpStatus.UNSUPPORTED_MEDIA_TYPE
                , HttpStatus.Series.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE).name()), REQUESTED_RANGE_NOT_SATISFIABLE(416
                , "Requested range not satisfiable"
                , "【RFC 7233】客户端请求的范围无效 —— 客户端已经要求文件的一部分（Byte serving），但服务器不能提供该部分。"
                .concat("\n如果请求中包含了 <code>Range</code> 请求头，并且 <code>Range</code> 中指定的任何数据范围都与当前资源的可用范围不重合，同时请求中又没有定义 <code>If-Range</code> 请求头，那么服务器就应当返回 <code>416</code> 状态码。")
                .concat("\n例如，如果客户端要求文件的一部分超出文件尾端。")
                , HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE
                , HttpStatus.Series.valueOf(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).name()), EXPECTATION_FAILED(417
                , "Expectation Failed"
                , "服务器无法满足 <code>Expect</code> 请求标头字段指示的期望值。"
                , HttpStatus.EXPECTATION_FAILED
                , HttpStatus.Series.valueOf(HttpStatus.EXPECTATION_FAILED).name()), I_AM_A_TEAPOT(418
                , "I'm a teapot"
                , "【彩蛋】【RFC 2324】“服务器拒绝尝试用茶壶冲泡咖啡”。"
                .concat("\n【彩蛋】当一个控制茶壶的 HTCPCP 收到 BREW 或 POST 指令要求其煮咖啡时应当回传此错误。")
                .concat("\n【说明】本操作码是在1998年作为 <a href=\"https://zh.wikipedia.org/wiki/%E4%BA%92%E8%81%94%E7%BD%91%E5%B7%A5%E7%A8%8B%E4%BB%BB%E5%8A%A1%E7%BB%84\">IETF</a> 的传统愚人节笑话, ")
                .concat("在 <code>RFC 2324</code> '超文本咖啡壶控制协议'中定义的，并不需要在真实的 HTTP 服务器中定义。")
                .concat("\n【应用实例】这个 HTTP 状态码在某些网站（包括 Google.com）与项目（如 Node.js、ASP.NET 和 Go 语言）中用作彩蛋。")
                , HttpStatus.I_AM_A_TEAPOT
                , HttpStatus.Series.valueOf(HttpStatus.I_AM_A_TEAPOT).name()), MISDIRECTED_REQUEST(421
                , "Misdirected Request"
                , "【RFC 7540】该请求针对的是无法产生响应的服务器（例如因为连接重用）。"
                .concat("\n这可以由服务器发送，该服务器未配置为针对包含在请求 URI 中的方案和权限的组合产生响应。")
                , null
                , HttpStatus.Series.valueOf(HttpStatus.BAD_REQUEST).name()), UNPROCESSABLE_ENTITY(422
                , "Unprocessable Entity"
                , "【WebDAV; RFC 4918】请求格式正确，但是由于含有语义错误，服务器无法响应。"
                , HttpStatus.UNPROCESSABLE_ENTITY
                , HttpStatus.Series.valueOf(HttpStatus.UNPROCESSABLE_ENTITY).name()), LOCKED(423
                , "Locked"
                , "【WebDAV; RFC 4918】当前资源被锁定。"
                , HttpStatus.LOCKED
                , HttpStatus.Series.valueOf(HttpStatus.LOCKED).name()), FAILED_DEPENDENCY(424
                , "Failed Dependency"
                , "【WebDAV; RFC 4918】由于之前的某个请求的失败，导致当前请求失败。"
                .concat("\n例如 PROPPATCH。")
                , HttpStatus.FAILED_DEPENDENCY
                , HttpStatus.Series.valueOf(HttpStatus.FAILED_DEPENDENCY).name()), TOO_EARLY(425
                , "Too Early"
                , "【RFC 8470】服务器不愿意冒着风险去处理可能重播的请求。（服务器拒绝处理在Early Data中的请求，以规避可能的重放攻击。）"
                , HttpStatus.TOO_EARLY
                , HttpStatus.Series.valueOf(HttpStatus.TOO_EARLY).name()), UPGRADE_REQUIRED(426
                , "Upgrade Required"
                , "【RFC 2817】服务器拒绝使用当前协议执行请求，但可能在客户端升级到其他协议后愿意执行请求；服务器应该在该响应中发送 Upgrade 头以指示所需的协议。"
                .concat("\n客户端应切换到 Upgrade 头字段中给出的（不同于当前请求的）协议。例如 TLS/1.0。")
                , HttpStatus.UPGRADE_REQUIRED
                , HttpStatus.Series.valueOf(HttpStatus.UPGRADE_REQUIRED).name()), PRECONDITION_REQUIRED(428
                , "Precondition Required"
                , "【RFC 6585】原始服务器要求[当前请求]是[有条件的]。"
                .concat("\n该状态码旨在防止“丢失更新”的问题，即[客户端获取资源状态后，修改该状态并将其提交给服务器，但这期间第三方已经修改了服务器上的状态，从而导致冲突]。")
                , HttpStatus.PRECONDITION_REQUIRED
                , HttpStatus.Series.valueOf(HttpStatus.PRECONDITION_REQUIRED).name()), TOO_MANY_REQUESTS(429
                , "Too Many Requests"
                , "【RFC 6585】用户在给定的时间内发送了太多的请求。"
                .concat("\n旨在用于网络限速。")
                .concat("\n【参见】<a href=\"https://en.wikipedia.org/wiki/Rate_limiting\">速率限制</a>")
                , HttpStatus.TOO_MANY_REQUESTS
                , HttpStatus.Series.valueOf(HttpStatus.TOO_MANY_REQUESTS).name()), REQUEST_HEADER_FIELDS_TOO_LARGE(431
                , "Request Header Fields Too Large"
                , "【RFC 6585】服务器不愿意处理该请求，因为它的请求头字段太大。"
                .concat("\n请求可以在减小请求头字段的大小后重新提交。")
                , HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE
                , HttpStatus.Series.valueOf(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE).name()), UNAVAILABLE_FOR_LEGAL_REASONS(451
                , "Unavailable For Legal Reasons"
                , "该访问因法律的要求而被拒绝。"
                .concat("\n例如：由政府审查的网页。")
                .concat("\n由 <a href=\"https://zh.wikipedia.org/wiki/%E4%BA%92%E8%81%94%E7%BD%91%E5%B7%A5%E7%A8%8B%E4%BB%BB%E5%8A%A1%E7%BB%84\">IETF</a> 在2015年核准后新增加。")
                , HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS
                , HttpStatus.Series.valueOf(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).name())

        //----- 5xx Server Error -----//

        , INTERNAL_SERVER_ERROR(500
                , "Internal Server Error"
                , "（通用错误消息）服务器遇到了一个未曾预料到的状况，导致它无法完成对请求的处理。"
                .concat("\n服务器没有给出具体错误信息。")
                , HttpStatus.INTERNAL_SERVER_ERROR
                , HttpStatus.Series.valueOf(HttpStatus.INTERNAL_SERVER_ERROR).name()), NOT_IMPLEMENTED(501
                , "Not Implemented"
                , "此请求方法不被服务器支持且无法被处理。"
                .concat("\n例如，网络服务 API 的新功能")
                .concat("\n只有 GET 和 HEAD 是要求服务器支持的，它们必定不会返回此错误代码。")
                , HttpStatus.NOT_IMPLEMENTED
                , HttpStatus.Series.valueOf(HttpStatus.NOT_IMPLEMENTED).name()), BAD_GATEWAY(502
                , "Bad Gateway"
                , "此错误响应表明服务器作为网关需要得到一个处理这个请求的响应，但是得到的却是一个错误的响应。"
                .concat("\n作为网关或者代理工作的服务器尝试执行请求时，从上游服务器接收到无效的响应。")
                , HttpStatus.BAD_GATEWAY
                , HttpStatus.Series.valueOf(HttpStatus.BAD_GATEWAY).name()), SERVICE_UNAVAILABLE(503
                , "Service Unavailable"
                , "由于临时的服务器维护或者过载，服务器当前无法处理请求。这个状况是暂时的，并且将在一段时间以后恢复。    "
                .concat("\n如果能够预计延迟时间，那么响应中可以包含一个 <code>Retry-After</code> 头用以标明这个延迟时间。")
                .concat("\n如果没有给出这个 <code>Retry-After</code> 信息，那么客户端应当以处理 <code>500</code> 响应的方式处理它。")
                .concat("\n【注意】与此响应一起，应发送解释问题的用户友好页面。")
                .concat("\n【注意】网站管理员还必须注意与此响应一起发送的与缓存相关的标头，因为这些临时条件响应通常不应被缓存。")
                , HttpStatus.SWITCHING_PROTOCOLS
                , HttpStatus.Series.valueOf(HttpStatus.SWITCHING_PROTOCOLS).name()), GATEWAY_TIMEOUT(504
                , "Gateway Timeout"
                , "作为网关或者代理工作的服务器尝试执行请求时，未能及时从上游服务器（URI 标识出的服务器，例如 HTTP、FTP、LDAP）或者辅助服务器（例如 DNS）收到响应。"
                .concat("\n【注意】某些代理服务器在 DNS 查询超时时会返回 <code>400</code> 或者 <code>500</code> 错误。")
                , HttpStatus.GATEWAY_TIMEOUT
                , HttpStatus.Series.valueOf(HttpStatus.GATEWAY_TIMEOUT).name()), HTTP_VERSION_NOT_SUPPORTED(505
                , "HTTP Version not supported"
                , "服务器不支持，或者拒绝支持请求中所使用的 HTTP 版本。"
                .concat("\n响应中应当包含一个[说明版本不被支持的原因]以及[服务器支持哪些协议]的实体。")
                , HttpStatus.HTTP_VERSION_NOT_SUPPORTED
                , HttpStatus.Series.valueOf(HttpStatus.HTTP_VERSION_NOT_SUPPORTED).name()), VARIANT_ALSO_NEGOTIATES(506
                , "Variant Also Negotiates"
                , "【RFC 2295】服务器有一个内部配置错误：对请求的透明内容协商导致循环引用。"
                .concat("\n（？？？没见过的状态码，仅看 WIKI 的说明也弄不透彻，需要了解相关协议；用到的时候再说）")
                , HttpStatus.VARIANT_ALSO_NEGOTIATES
                , HttpStatus.Series.valueOf(HttpStatus.VARIANT_ALSO_NEGOTIATES).name()), INSUFFICIENT_STORAGE(507
                , "Insufficient Storage"
                , "【WebDAV; RFC 4918】服务器无法存储完成请求所必须的内容。"
                .concat("这个状况被认为是临时的。")
                , HttpStatus.INSUFFICIENT_STORAGE
                , HttpStatus.Series.valueOf(HttpStatus.INSUFFICIENT_STORAGE).name()), LOOP_DETECTED(508
                , "Loop Detected"
                , "【WebDAV; RFC 5842】服务器在处理请求时检测到无限循环。（可代替 <code>208</code> 状态码）"
                , HttpStatus.LOOP_DETECTED
                , HttpStatus.Series.valueOf(HttpStatus.LOOP_DETECTED).name()), NOT_EXTENDED(510
                , "Not Extended"
                , "【RFC 2774】获取资源所需要的策略并没有被满足 —— 客户端需要对请求进一步扩展，服务器才能实现它。"
                .concat("\n服务器会回复客户端发出扩展请求所需的所有信息。")
                , HttpStatus.NOT_EXTENDED
                , HttpStatus.Series.valueOf(HttpStatus.NOT_EXTENDED).name()), NETWORK_AUTHENTICATION_REQUIRED(511
                , "Network Authentication Required"
                , "客户端需要进行身份验证才能获得网络访问权限。用于限制用户群访问特定网络。"
                .concat("\n常见应用场景：")
                .concat("\n-> · 连接 WiFi 热点时的强制网络门户")
                .concat("\n【参见】<a href=\"https://zh.wikipedia.org/wiki/%E5%BC%BA%E5%88%B6%E9%97%A8%E6%88%B7\">强制门户</a>")
                , HttpStatus.NETWORK_AUTHENTICATION_REQUIRED
                , HttpStatus.Series.valueOf(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).name());

        /**
         * 为持久化类型转换器提供支持
         */
        @javax.persistence.Converter(autoApply = true)
        public static class Converter
                extends VoAttributeConverter<StatusVo, Integer, String> {

            // (单例模式 - 登记式)
            private static class Factory {
                private static final Converter SINGLETON = new Converter();
            }

            private Converter() {
                super(StatusVo.class);
            }

            @NotNull
            public static Converter getInstance() {
                return Converter.Factory.SINGLETON;
            }

        }

        public final int code;

        @NotNull
        public final String name;

        @NotNull
        public final String description;

        @Nullable
        public final HttpStatus httpStatus;

        @NotNull
        public final String type;

        /**
         * (Constructor)
         *
         * @param code
         * @param name
         * @param description
         * @param httpStatus
         * @param type
         * @throws IllegalArgumentException
         */
        StatusVo(int code
                , @NotNull String name
                , @NotNull String description
                , @Nullable HttpStatus httpStatus
                , @NotNull String type)
                throws IllegalArgumentException {
            if (null == name) {
                throw new IllegalArgumentException("非法输入: <param>name</param>");
            }
            if (null == description) {
                throw new IllegalArgumentException("非法输入: <param>description</param>");
            }
            if (null == type) {
                throw new IllegalArgumentException("非法输入: <param>type</param>");
            }

            this.code = code;
            this.name = name;
            this.description = description;
            this.httpStatus = httpStatus;
            /*this.type = HttpStatus.Series.valueOf(this.httpStatus).name();*/
            this.type = type;
        }

        /**
         * VO 的值
         *
         * @return {@link Integer}  可为 <code>null</code>
         * @Description Unique attribute.
         */
        @Override
        public Integer value() {
            return this.code;
        }

        /**
         * VO 的详细信息
         *
         * @return {@link String}
         */
        @Override
        public String description() {
            return this.description;
        }

        /**
         * VO 的 (展示)名称
         *
         * @return {@link String}
         */
        @NotNull
        @Override
        public String displayName() {
            return this.name;
        }

        @Override
        public String toString() {
            return VoModel.toString(this);
        }

        /**
         * 提供类型转换器
         *
         * @return {@link Converter}
         * @Design 为持久化类型转换功能提供支持.
         */
        @NotNull
        @Override
        @SuppressWarnings("unchecked")
        public Converter voAttributeConverter() {
            return Converter.getInstance();
        }

        /**
         * 等效比较
         *
         * @param name HTTP Method name
         * @return 比较结果
         */
        public boolean equals(String name) {
            return this.name().equals(name);
        }

    }

}
