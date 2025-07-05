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
package org.htmlunit.jsoup.example;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.jsoup.HtmlUnitDOMToJsoupConverter;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.html2md.converter.LinkConversion;
import com.vladsch.flexmark.util.data.MutableDataSet;

/**
 * Sample class demonstrating how to use HtmlUnit and Jsoup together with Flexmark
 * to convert HTML content from web pages into Markdown format.
 *
 * <p>This class provides examples of two common use cases:
 * <ul>
 * <li>Converting an entire web page to Markdown</li>
 * <li>Converting a specific part of a web page to Markdown using CSS selectors</li>
 * </ul>
 *
 * <p>The conversion process involves:
 * <ol>
 * <li>Fetching the web page using HtmlUnit's WebClient</li>
 * <li>Converting the HtmlUnit DOM to Jsoup's Document format</li>
 * <li>Using Flexmark to convert the HTML to Markdown</li>
 * </ol>
 *
 * @author Ronald Brill
 */
public class FlexmarkConverterSample {

    /**
     * Demonstrates converting an entire web page to Markdown format.
     *
     * <p>This test method:
     * <ul>
     * <li>Fetches a web page using HtmlUnit's WebClient</li>
     * <li>Converts the entire page from HtmlUnit DOM to Jsoup Document</li>
     * <li>Uses Flexmark to convert the HTML content to Markdown</li>
     * <li>Prints the resulting Markdown to the console</li>
     * </ul>
     *
     * @throws Exception if any error occurs during web page fetching or conversion
     */
    @Test
    public void convertWholePageToMarkdown() throws Exception {
        final String url = "https://docs.mendix.com/apidocs-mxsdk/apidocs/pluggable-widgets-property-types/";

        try (WebClient client = new WebClient()) {
            client.getOptions().setThrowExceptionOnScriptError(false);
            client.getOptions().setJavaScriptEnabled(true);
            client.getOptions().setCssEnabled(false);

            final HtmlPage page = client.getPage(url);

            // convert from HtmlUnit into Jsoup world
            final HtmlUnitDOMToJsoupConverter converter = HtmlUnitDOMToJsoupConverter.builder().build();
            final org.jsoup.nodes.Node jsoupNode = converter.convert(page);

            final MutableDataSet options = new MutableDataSet();
            options.set(FlexmarkHtmlConverter.EXT_INLINE_LINK, LinkConversion.MARKDOWN_EXPLICIT);

            final String markdown = FlexmarkHtmlConverter.builder(options).build().convert(jsoupNode);
            System.out.println(markdown);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates converting a specific part of a web page to Markdown format using CSS selectors.
     *
     * <p>This test method:
     * <ul>
     * <li>Fetches a web page using HtmlUnit's WebClient</li>
     * <li>Converts the entire page from HtmlUnit DOM to Jsoup Document</li>
     * <li>Selects a specific element using a CSS selector (".highlight")</li>
     * <li>Converts only the selected element to Markdown using Flexmark</li>
     * <li>Prints the resulting Markdown to the console</li>
     * </ul>
     *
     * <p>This approach is useful when you only need to convert a specific section
     * of a web page rather than the entire content, which can result in cleaner
     * and more focused Markdown output.
     *
     * <p><strong>Note:</strong>You have to convert the whole page first (before selection),
     * as this ensures the element selected later has a parent element, which is
     * important for proper DOM structure, CSS selector functionality and the converter.
     *
     * @throws Exception if any error occurs during web page fetching, element selection, or conversion
     */
    @Test
    public void convertPagePartToMarkdown() throws Exception {
        final String url = "https://docs.mendix.com/apidocs-mxsdk/apidocs/pluggable-widgets-property-types/";
        final String contentSelector = ".highlight";

        try (WebClient client = new WebClient()) {
            client.getOptions().setThrowExceptionOnScriptError(false);
            client.getOptions().setJavaScriptEnabled(true);
            client.getOptions().setCssEnabled(false);

            final HtmlPage page = client.getPage(url);

            // convert from HtmlUnit into Jsoup world
            // usually it is a good idea to convert the whole page; this ensures
            // the element we select below has a parent
            final HtmlUnitDOMToJsoupConverter converter = HtmlUnitDOMToJsoupConverter.builder().build();
            final org.jsoup.nodes.Document jsoupDocument = (Document) converter.convert(page);

            final org.jsoup.nodes.Node jsoupElementNode = jsoupDocument.selectFirst(contentSelector);

            final MutableDataSet options = new MutableDataSet();
            options.set(FlexmarkHtmlConverter.EXT_INLINE_LINK, LinkConversion.MARKDOWN_EXPLICIT);

            final String markdown = FlexmarkHtmlConverter.builder(options).build().convert(jsoupElementNode);
            System.out.println(markdown);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
