/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lisuke.pa_wzry;

import com.lisuke.pa_wzry.json.HeroListJson;
import com.lisuke.pa_wzry.json.EquipItemJson;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlItalic;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.DatabaseConnection;
import com.lisuke.pa_wzry.DBUtils.DatabaseHelper;
import com.lisuke.pa_wzry.Model.Equip;
import com.lisuke.pa_wzry.Model.EquipCate;
import com.lisuke.pa_wzry.Model.Hero;
import com.lisuke.pa_wzry.Model.HeroCate;
import com.lisuke.pa_wzry.Model.HeroEquipSuggest;
import com.lisuke.pa_wzry.Model.HeroSkill;
import com.lisuke.pa_wzry.json.JSONUtils;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lisuke
 */
public class Main {
    static WebClient c = new WebClient();
    String URL = "http://pvp.qq.com/web201605/herolist.shtml";
    
    Main() {
        c.getOptions().setJavaScriptEnabled(true);
        c.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.76 Safari/537.36");
        c.getOptions().setThrowExceptionOnFailingStatusCode(false);
        c.setAjaxController(new NicelyResynchronizingAjaxController());//设置ajax代理
        c.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
        c.getOptions().setCssEnabled(false);
    }
    
    private String getJsonString(WebClient c,String url,String decode) throws IOException{
        Page page = c.getPage(url);
        return page.getWebResponse().getContentAsString(decode);
    }
    
    public void initEquipCate(){
        EquipCate cate1 = new EquipCate();
        cate1.setEquipCateId(1);
        cate1.setEquipCateName("攻击");
        
        EquipCate cate2 = new EquipCate();
        cate2.setEquipCateId(2);
        cate2.setEquipCateName("法术");
        
        EquipCate cate3 = new EquipCate();
        cate3.setEquipCateId(3);
        cate3.setEquipCateName("防御");
        
        EquipCate cate4 = new EquipCate();
        cate4.setEquipCateId(4);
        cate4.setEquipCateName("移动");
        
        EquipCate cate5 = new EquipCate();
        cate5.setEquipCateId(5);
        cate5.setEquipCateName("打野");
        
        try {
            Dao dao = DatabaseHelper.getDao(EquipCate.class);
            dao.create(cate1);
            dao.create(cate2);
            dao.create(cate3);
            dao.create(cate4);
            dao.create(cate5);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getEquip() throws IOException
    {
        String equiplist_json = "http://pvp.qq.com/web201605/js/item.json";
        String json = getJsonString(c,equiplist_json,"UTF-8");
        LinkedList<EquipItemJson> beanList = new JSONUtils<EquipItemJson>().jsonToArray(json, EquipItemJson.class);
        Dao equipCateDao = null;
        Dao equipDao = null;
        try {
            equipCateDao =  DatabaseHelper.getDao(EquipCate.class);
            equipDao =  DatabaseHelper.getDao(Equip.class);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i = 0 ; i< beanList.size(); i++ ){
            EquipItemJson js = beanList.get(i);
            Equip e = new Equip();
            e.setDes1(js.getDes1());
            e.setDes2(js.getDes2());
            EquipCate cate = null;
            try {
                cate = (EquipCate) equipCateDao.queryForId(js.getItem_type());
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.setEquipCate(cate);
            e.setEquipId(js.getItem_id());
            e.setPrice(js.getPrice());
            e.setTotalPrice(js.getTotal_price());
            e.setEquipName(js.getItem_name());
            try {
                //DatabaseConnection dc = equipDao.startThreadConnection();
                //equipDao.setAutoCommit(dc, false);
                equipDao.create(e);
//                equipDao.commit(dc);
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        System.out.println(beanList.size());
    }
    
    List<HeroListJson> getHeroList() throws IOException{
        String herolist_json = "http://pvp.qq.com/web201605/js/herolist.json";
        String json = getJsonString(c,herolist_json,"UTF-8");
        List<HeroListJson> beanList = new JSONUtils<HeroListJson>().jsonToArray(json, HeroListJson.class);
        System.out.println(beanList);
        return beanList;
    }
    
    public void getHeroInfo() throws IOException
    {
        List<HeroListJson> list = getHeroList();
        
        Dao heroCateDao = null;
        Dao heroDao = null;
        try {
            heroCateDao =  DatabaseHelper.getDao(HeroCate.class);
            heroDao =  DatabaseHelper.getDao(Hero.class);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i = 0 ; i < list.size();i++){
            HeroListJson json = list.get(i);
            Hero hero = new Hero();//hero
            //英雄Id 
            hero.setHeroId(json.getEname());
            //英雄名称
            hero.setHeroName(json.getCname());
            HeroCate heroCate = null;
            try {
                heroCate = (HeroCate) heroCateDao.queryForId(json.getHero_type());
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            // 英雄类型
            hero.setHeroCate(heroCate);
            String herolist_json = "http://pvp.qq.com/web201605/herodetail/" + hero.getHeroId() + ".shtml";
            HtmlPage page = c.getPage(herolist_json);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            //生存能力
            HtmlItalic i_tag= (HtmlItalic) page.getFirstByXPath("/html/body/div[1]/div[12]/div/div[1]/div[2]/div/div[1]/ul/li[1]/span/i");
            String str = i_tag.getAttribute("style");
            hero.setHeroViability(Integer.valueOf(str.substring(6, str.length()-1)));
            //攻击伤害
            i_tag= (HtmlItalic) page.getFirstByXPath("/html/body/div[1]/div[12]/div/div[1]/div[2]/div/div[1]/ul/li[2]/span/i");
            str = i_tag.getAttribute("style");
            hero.setHeroAttackDamage(Integer.valueOf(str.substring(6, str.length()-1)));
            //技能效果
            i_tag= (HtmlItalic) page.getFirstByXPath("/html/body/div[1]/div[12]/div/div[1]/div[2]/div/div[1]/ul/li[3]/span/i");
            str = i_tag.getAttribute("style");
            hero.setHeroSkillDamage(Integer.valueOf(str.substring(6, str.length()-1)));
            //上手难度
            i_tag= (HtmlItalic) page.getFirstByXPath("/html/body/div[1]/div[12]/div/div[1]/div[2]/div/div[1]/ul/li[4]/span/i");
            str = i_tag.getAttribute("style");
            hero.setHeroOverHand(Integer.valueOf(str.substring(6, str.length()-1)));
            
            HtmlParagraph backStory = page.getFirstByXPath("/html/body/div[1]/div[12]/div/div[2]/div[1]/div[2]/div[1]/p[1]");
            hero.setHeroBackStory(backStory.asText());
            HtmlDivision heroAnnal = page.getFirstByXPath("//*[@id=\"history_content\"]");
            hero.setHeroAnnal(heroAnnal.asText());
            
            try {
                heroDao.create(hero);
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            ////////
            // 技能
            HtmlDivision skill_show= page.getFirstByXPath("/html/body/div[1]/div[12]/div/div[3]/div[1]/div/div");
            //System.out.println(skill_show.asText());
            HtmlImage image1 = page.getFirstByXPath("/html/body/div[1]/div[12]/div/div[3]/div[2]/div[4]/span[2]/img");
            String src = image1.getAttribute("src");
            System.out.println(str);
            int major = Integer.valueOf(src.substring(src.length()-6, src.length()-5));
            
            HtmlImage image2 = page.getFirstByXPath("/html/body/div[1]/div[12]/div/div[3]/div[2]/div[4]/span[2]/img");
            src = image2.getAttribute("src");
            int slave = Integer.valueOf(src.substring(src.length()-6, src.length()-5));
            for(int k = 0; k< skill_show.getChildElementCount();k++){
                
                HeroSkill skill = new HeroSkill();
                Dao heroSkillDao = null;
                try {
                    heroSkillDao = DatabaseHelper.getDao(HeroSkill.class);
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                HtmlAnchor skillName = (HtmlAnchor) page.getFirstByXPath("/html/body/div[1]/div[12]/div/div[3]/div[1]/div/div/div["+(k+1)+"]/a");
                HtmlParagraph coolingRate = (HtmlParagraph) page.getFirstByXPath("/html/body/div[1]/div[12]/div/div[3]/div[1]/div/div/div["+(k+1)+"]/p[1]");
                HtmlParagraph consume = (HtmlParagraph)page.getFirstByXPath("/html/body/div[1]/div[12]/div/div[3]/div[1]/div/div/div["+(k+1)+"]/p[2]");
                HtmlParagraph description = (HtmlParagraph)page.getFirstByXPath("/html/body/div[1]/div[12]/div/div[3]/div[1]/div/div/div["+(k+1)+"]/p[3]");
                skill.setHeroSkillName(skillName.asText());
                skill.setHero(hero);
                skill.setCoolingRate(coolingRate.asText());
                skill.setConsume(consume.asText());
                skill.setDescription(description.asText());
                if(major-- == 0)
                    skill.setSuggestPriority(2);
                else if(slave-- == 0)
                    skill.setSuggestPriority(1);
                else
                    skill.setSuggestPriority(0);
                
                System.out.println(skill);
                try {
                    heroSkillDao.create(skill);
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            ////////
            // 出装
            // 早期
            //System.out.println(hero);
            HtmlUnorderedList ul = page.getFirstByXPath("/html/body/div[1]/div[12]/div/div[4]/div[2]/div[1]/ul");
            String tmp = ul.getAttribute("data-item");
            int equipId = Integer.valueOf(tmp);
            Dao equipDao = null;
            Dao heroEquipSuggestDao = null;
            HeroEquipSuggest suggest = new HeroEquipSuggest();
            suggest.setHero(hero);
            suggest.setSuggestEquipId(0);
            try {
                equipDao = DatabaseHelper.getDao(Equip.class);
                heroEquipSuggestDao = DatabaseHelper.getDao(HeroEquipSuggest.class);
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                Equip equip = (Equip) equipDao.queryForId(equipId);
                suggest.setEquip(equip);
                heroEquipSuggestDao.create(suggest);
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //中后期
            String xpath = "/html/body/div[1]/div[12]/div/div[4]/div[2]/div[3]/ul";
            ul = page.getFirstByXPath(xpath);
            String items = ul.getAttribute("data-item");
            System.out.println(items);
            if(items.startsWith("|"))
                items = items.replaceFirst("\\|", "");
            if(items.endsWith("|"))
                items = items.substring(0, items.length());
            items = items.replaceAll("\\|\\|", "\\|");
            String [] item = items.split("\\|");
            for(int index = 1;index<= item.length ;index++){
                equipId = Integer.valueOf(item[index-1].trim());
                HeroEquipSuggest sugg = new HeroEquipSuggest();
                try {
                    Equip equip = (Equip) equipDao.queryForId(equipId);
                    sugg.setEquip(equip);
                    sugg.setHero(hero);
                    sugg.setHeroEquipSuggestIndex(index);
                    heroEquipSuggestDao.create(sugg);
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    Main(String URL) {
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
    

    private void test() throws IOException, SQLException {

        Dao dao = DatabaseHelper.getDao(HeroCate.class);
        HeroCate cate = (HeroCate) dao.queryForId(1);
        //cate.getAllEquip();
        ForeignCollection<Hero> es =cate.getAllHero();
        //es.refreshAll();
        for ( Hero item : es) {
            System.out.println(item.toString());
        }
        
    }

    private void initHeroCate() {
        HeroCate cate1 = new HeroCate();
        cate1.setHeroCateId(1);
        cate1.setHeroCateName("战士");
        
        HeroCate cate2 = new HeroCate();
        cate2.setHeroCateId(2);
        cate2.setHeroCateName("法师");
        
        HeroCate cate3 = new HeroCate();
        cate3.setHeroCateId(3);
        cate3.setHeroCateName("坦克");
        
        HeroCate cate4 = new HeroCate();
        cate4.setHeroCateId(4);
        cate4.setHeroCateName("刺客");
        
        HeroCate cate5 = new HeroCate();
        cate5.setHeroCateId(5);
        cate5.setHeroCateName("射手");
        
        HeroCate cate6 = new HeroCate();
        cate6.setHeroCateId(6);
        cate6.setHeroCateName("辅助");
        
        try {
            Dao dao = DatabaseHelper.getDao(HeroCate.class);
            dao.create(cate1);
            dao.create(cate2);
            dao.create(cate3);
            dao.create(cate4);
            dao.create(cate5);
            dao.create(cate6);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String [] args) throws SQLException, IOException{
        
        Main main = new Main();
        
        main.initEquipCate();
        main.initHeroCate();
        main.getEquip();
        main.getHeroInfo();
        
        //main.test();
        
        
       /* */
        /*
        String str = "1326|1423|1226|1336|12211|1238";
        
        System.out.println(str.split("\\|").length);
        /*
        String str= "width:60%";
        System.out.println(Integer.valueOf(str.substring(6, str.length()-1)));
        */
        /*
        String str = new File("http://game.gtimg.cn/images/yxzj/img201606/heroimg/182/18210.png").getName();
        Integer.valueOf(str.substring(0, str.length()-4));
        System.out.println(Integer.valueOf(str.substring(0, str.length()-4)));
        /**/
    }
}
