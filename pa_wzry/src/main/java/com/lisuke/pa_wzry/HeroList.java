/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lisuke.pa_wzry;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author lisuke
 */
public class HeroList {
    static WebClient c = new WebClient();
    String URL = "http://pvp.qq.com/web201605/herolist.shtml";
    
    HeroList() {
        c.getOptions().setJavaScriptEnabled(true);
        c.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.76 Safari/537.36");
        c.getOptions().setThrowExceptionOnFailingStatusCode(false);
        c.setAjaxController(new NicelyResynchronizingAjaxController());//设置ajax代理
        c.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常

    }
    HeroList(String URL) {
        this.URL = URL;
    }
    
    public void init() throws IOException{
        new File("/home/lisuke/wzry/").mkdirs();
        
        HtmlPage page = c.getPage(URL);
        //System.out.println(page.asText());
        
        List<HtmlListItem> li = (List<HtmlListItem>)  page.getByXPath("/html/body/div[1]/div[12]/div/div[2]/div[2]/ul/li");
        System.out.print(li.size());
        for(int i = 0; i < li.size();i++)
        {
            HtmlAnchor a = (HtmlAnchor) li.get(i).getFirstChild();
            System.out.println(a.asText());
            HtmlImage image = (HtmlImage) a.getFirstChild();
            String src = image.getSrcAttribute();
            image.saveAs(new File("/home/lisuke/wzry/hero_"+new File(image.getAttribute("src")).getName()));
            System.out.println(src);
        }
    }
    public static void main(String args[]) throws IOException
    {
        HeroList state = new HeroList();
        state.init();
        
    }
}
