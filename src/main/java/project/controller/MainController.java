package project.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.model.Vote;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class MainController {
    private static double countVotesYes = 0.0;
    private static double countVotesNo = 0.0;

    @Value("${welcome.message:test}")
    private String message = "Hello World";

    @RequestMapping("/")
    public String home(Map<String, Object> model) {
        model.put("message", this.message);
        return "home";
    }

    public double counts() {
        try {
            double first = 100 / (countVotesNo + countVotesYes);
            double second = first * (countVotesYes);
            if(second > 100)
                return 100;
            return second;
        } catch (Exception e) {
            System.out.println("ex");
            if((int)countVotesNo==0)
                return 100;
            else return 0;
        }
    }

    public String getString() {
        String h = " <circle class=\"donut-hole\" cx=\"21\" cy=\"21\" r=\"15.91549430918954\" fill=\"#fff\"></circle>\n" +
                "                <circle class=\"donut-ring\" cx=\"21\" cy=\"21\" r=\"15.91549430918954\" fill=\"transparent\" stroke=\"#d2d3d4\" stroke-width=\"3\"></circle>\n" +
                "                <circle class=\"donut-segment\" cx=\"21\" cy=\"21\" r=\"15.91549430918954\" fill=\"transparent\" stroke=\"#ce4b99\" stroke-width=\"3\" stroke-dasharray=\""+counts()+" "+(100.0-counts())+"\" stroke-dashoffset=\"25\"></circle>\n" +
                "<g class=\"chart-text\">\n" +
                "    <text x=\"50%\" y=\"50%\" class=\"chart-number\">\n" +
                "      "+(int)counts()+"\n" +
                "    </text>\n" +
                "    <text x=\"50%\" y=\"50%\" class=\"chart-label\">\n" +
                "      percent\n" +
                "    </text>\n" +
                "  </g>" +
                "            " +
                "<style>\n" +
                "@import url(https://fonts.googleapis.com/css?family=Montserrat:400);\n" +
                ".chart-text {\n" +
                "  font: 16px/1.4em \"Montserrat\", Arial, sans-serif;\n" +
                "  fill: #000;\n" +
                "  -moz-transform: translateY(0.25em);\n" +
                "  -ms-transform: translateY(0.25em);\n" +
                "  -webkit-transform: translateY(0.25em);\n" +
                "  transform: translateY(0.25em);\n" +
                "}\n" +
                ".chart-number {\n" +
                "  font-size: 0.6em;\n" +
                "  line-height: 1;\n" +
                "  text-anchor: middle;\n" +
                "  -moz-transform: translateY(-0.25em);\n" +
                "  -ms-transform: translateY(-0.25em);\n" +
                "  -webkit-transform: translateY(-0.25em);\n" +
                "  transform: translateY(-0.25em);\n" +
                "}\n" +
                ".chart-label {\n" +
                "  font-size: 0.2em;\n" +
                "  text-transform: uppercase;\n" +
                "  text-anchor: middle;\n" +
                "  -moz-transform: translateY(0.7em);\n" +
                "  -ms-transform: translateY(0.7em);\n" +
                "  -webkit-transform: translateY(0.7em);\n" +
                "  transform: translateY(0.7em);\n" +
                "}\n" +
                "</style>";
        return h;
    }

    @GetMapping("/main_page")
    public String MainPage(Model model, Map<String, Object> myVarYes, Map<String, Object> myVarNo, Map<String, Object> str) {
        str.put("str", getString());
        myVarYes.put("myVarYes", countVotesYes);
        myVarNo.put("myVarNo", countVotesNo);
        model.addAttribute("theTempBean", new Vote());
        return "main_page";
    }

    @PostMapping("/main_page")
    public String MainPaige(@ModelAttribute("theTempBean") @Valid Vote value, Map<String, Object> myVarYes, Map<String, Object> myVarNo, Map<String, Object> str) {
        try {
            if (value.getVoteYes()!=null) {
                countVotesYes += 1.0;
            }
            else if(value.getVoteNo() != null) {
                countVotesNo+=1.0;
            }
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
        myVarYes.put("myVarYes", countVotesYes);
        myVarNo.put("myVarNo", countVotesNo);
        str.put("str", getString());
        System.out.println(counts());
        System.out.println(countVotesYes + " no = " + countVotesNo);
        return "main_page";
    }

    @GetMapping("/falsify_page")
    public String falsifyPage(Model model) {
        model.addAttribute("theTempBean", new Vote());
        return "falsify_page";
    }

    @PostMapping("/falsify_page")
    public String falsifyPaige(@ModelAttribute("theTempBean") @Valid Vote value) {
        try {
            if(value.getVoteNo().equals("yes"))
                countVotesYes+=Integer.parseInt(value.getVoteYes());
            else
                countVotesNo+=Integer.parseInt(value.getVoteYes());
        } catch (Exception e) {}
        return "redirect:/main_page";
    }
}
