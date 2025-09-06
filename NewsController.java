package com.example.newsinteractive.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class NewsController {

  @GetMapping("/news")
  public List<Map<String,Object>> news(@RequestParam(required=false) String q,
                                      @RequestParam(required=false) String cat,
                                      @RequestParam(defaultValue="0") int offset){
    // sample dataset
    List<Map<String,Object>> list = new ArrayList<>();
    list.add(article("Big Tech Launches New AI Tool","Companies unveil new AI tools to accelerate workflows.","TechDaily","https://tech.example/ai","https://via.placeholder.com/220x120","tech"));
    list.add(article("Market Rally Continues","Markets show resilience amid macro optimism.","WorldFinance","https://world.example/market","https://via.placeholder.com/120x80","business"));
    list.add(article("New Java Release","Java 21 brings performance improvements.","DevNews","https://dev.example/java","https://via.placeholder.com/120x80","tech"));
    list.add(article("Local Sports Upset","Underdog wins dramatic match.","SportTimes","https://sport.example/upset","https://via.placeholder.com/120x80","sports"));
    list.add(article("Startup Funding Spike","Early-stage funding increases this quarter.","BizInsider","https://biz.example/funding","https://via.placeholder.com/120x80","business"));
    // filter simple
    if(q!=null && !q.isBlank()){
      String Q=q.toLowerCase();
      list.removeIf(m -> !(m.get("title").toString().toLowerCase().contains(Q) || m.get("description").toString().toLowerCase().contains(Q)));
    }
    if(cat!=null && !cat.isBlank()){
      list.removeIf(m -> !cat.equalsIgnoreCase(m.get("category").toString()));
    }
    if(offset>0){
      return list.subList(Math.min(offset, list.size()), list.size());
    }
    return list;
  }

  @GetMapping("/sponsored")
  public List<Map<String,String>> sponsored(){
    return Arrays.asList(
      sponsor("SignalFire CTO Program","Accelerator program for founders","https://signal.example"),
      sponsor("Analytics Pro","Data analytics for teams","https://analytics.example")
    );
  }

  @GetMapping("/trending")
  public List<String> trending(){
    return Arrays.asList("AI", "Java 21", "Market Rally", "Startups");
  }

  private Map<String,Object> article(String title,String desc,String src,String url,String img,String category){
    Map<String,Object> m = new HashMap<>();
    m.put("title", title);
    m.put("description", desc);
    m.put("source", src);
    m.put("url", url);
    m.put("image", img);
    m.put("publishedAt", new Date().toString());
    m.put("category", category);
    return m;
  }

  private Map<String,String> sponsor(String t,String d,String u){
    Map<String,String> m = new HashMap<>();
    m.put("title", t); m.put("desc", d); m.put("url", u); return m;
  }
}
