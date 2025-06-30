/*
 * Copyright (c) 2025 Ronald Brill.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.jsoup;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.jsoup.utils.JSoupAssertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;
import org.junit.jupiter.api.Test;

public class HtmlUnitDOMToJsoupConverterTest {

    private void test(final String html) throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.loadHtmlCodeIntoCurrentWindow(html);

            final HtmlUnitDOMToJsoupConverter converter = HtmlUnitDOMToJsoupConverter.builder().build();
            final Node htmlunitNode = converter.convert(page);
            final Node jsoupNode = Jsoup.parse(html);

            JSoupAssertions.assertNodesEqual(jsoupNode, htmlunitNode);
        }
    }

    @Test
    public void empty() throws Exception {
        final String html =
                "<html></html>";
        test(html);
    }

    @Test
    public void bodyWithText() throws Exception {
        final String html =
                "<html><body>HtmlUnit</body></html>";
        test(html);
    }

    @Test
    public void bodyWithParagraph() throws Exception {
        final String html =
                "<html><body><p>HtmlUnit</p></body></html>";
        test(html);
    }

    @Test
    public void bodyWithTitle() throws Exception {
        final String html =
                "<html><body><h1>HtmlUnit</h1></body></html>";
        test(html);
    }

    @Test
    public void uppercaseTags() throws Exception {
        final String html =
                "<HTML><BODY><H1>HtmlUnit</H1></BODY></HTML>";
        test(html);
    }

    @Test
    public void mixedcaseTags() throws Exception {
        final String html =
                "<HTML><BodY><h1>HtmlUnit</H1></BODY></html>";
        test(html);
    }
}
