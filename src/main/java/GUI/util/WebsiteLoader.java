package gui.util;

import gui.Controller.ClientViewController;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 */
public class WebsiteLoader {
    private final static String GOOGLE = "http://www.google.com/search?q=";
    private WebEngine webEngine;

    public WebsiteLoader(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    public void addWebView(Pane websiteSpace){
        WebView webView = new WebView();
       // ClientViewController.setConstraints(webView);
        webEngine = webView.getEngine();
        webView.setZoom(0.6);
        webView.prefHeightProperty().bind(websiteSpace.heightProperty());
        webView.prefWidthProperty().bind(websiteSpace.widthProperty());
        websiteSpace.getChildren().add(webView);
    }

    public void executeQuery(String website){
        webEngine.load(GOOGLE+ website);
    }
}
