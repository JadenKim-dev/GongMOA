package GongMoa.gongmoa.jsoup;

import GongMoa.gongmoa.domain.Contest.ApplicationDate;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.domain.Contest.Host;
import GongMoa.gongmoa.domain.Contest.Prize;
import GongMoa.gongmoa.service.ContestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class Crawling {
    static final String thinkcontestUrl = "https://www.thinkcontest.com";

    private final ContestService contestService;

    public void doCrawling() {
        long beforeTime = System.currentTimeMillis();
        log.info("Crawling init");

        ArrayList<String> titleList = new ArrayList<>(2000);
        ArrayList<String> imgLinkList = new ArrayList<>(2000);
        ArrayList<HashMap<String, String>> attributesList = new ArrayList<>(2000);
        ArrayList<String> descriptionList = new ArrayList<>(2000);

        putInformationToLists(titleList, imgLinkList, attributesList, descriptionList);

//        imgLinkList.subList(0, 5).forEach(l -> log.info("image link={}", l));
//        for (int i = 0; i < 5; i++) {
//            attributesList.get(i).forEach((key, value)-> log.info("{}: {}", key, value));
//        }
//        descriptionList.subList(0, 5).forEach(l -> log.info("image link={}", l));
//        titleList.subList(0, 5).forEach(t -> log.info("title={}", t));

        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime)/1000;
        log.info("걸린 시간(초) = {}", secDiffTime);

        saveContests(titleList, imgLinkList, attributesList, descriptionList);
    }

    private void saveContests(ArrayList<String> titleList, ArrayList<String> imgLinkList, ArrayList<HashMap<String, String>> attributesList, ArrayList<String> descriptionList) {
        ArrayList<Contest> contests = new ArrayList<>(titleList.size());
        for (int i = 0; i < titleList.size(); i++) {
            HashMap<String, String> attributeMap = attributesList.get(i);
            LocalDate[] applicationDates = Arrays.stream(attributeMap.get("접수기간")
                            .replace(" ", "").split("~"))
                    .map(s -> LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE)).toArray(LocalDate[]::new);
            ApplicationDate applicationDate = new ApplicationDate(applicationDates[0], applicationDates[1]);

            Host host = new Host(
                    attributeMap.getOrDefault("주최", null),
                    attributeMap.getOrDefault("주관", null),
                    attributeMap.getOrDefault("후원/협찬", null));

            Prize prize = new Prize(
                    attributeMap.getOrDefault("1등 시상금", null),
                    null,
                    null);

            Contest contest = new Contest(
                    titleList.get(i), descriptionList.get(i), imgLinkList.get(i),
                    applicationDate, host, prize);
            contests.add(contest);
        }
        contestService.createContests(contests);
    }

    private static void putInformationToLists(
            ArrayList<String> titleList,
            ArrayList<String> imgLinkList,
            ArrayList<HashMap<String, String>> attributesList,
            ArrayList<String> descriptionList) {

        ArrayList<String> contestLinkList = getContestLinks();

        for (String contestLink : contestLinkList) {
            Connection conn = Jsoup.connect(thinkcontestUrl + contestLink);
            try{
                Document document = conn.get();

                titleList.add(document.getElementsByClass("title").get(1).text());

                imgLinkList.add(document.select("img#poster").get(0).attr("src"));

                Element tbodyTag = document.getElementsByTag("tbody").get(0);
                List<String> labels = tbodyTag.getElementsByTag("th")
                        .stream().map(Element::text).collect(Collectors.toList());
                List<String> contents = tbodyTag.getElementsByTag("td")
                        .stream().map(Element::text).collect(Collectors.toList());
                HashMap<String, String> attributes = new HashMap<>(15);
                for (int i = 0; i < labels.size(); i++) {
                    attributes.put(labels.get(i), contents.get(i));
                }
                attributesList.add(attributes);

                descriptionList.add(document.getElementsByClass("info-cont").get(0).html());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static ArrayList<String> getContestLinks() {
        ArrayList<String> contestLinks = new ArrayList<>(2000);

        for (int i = 1; i<=5; i++) {
            Connection conn = Jsoup.connect(thinkcontestUrl + "/Contest/CateField.html?page=" + i);
            try {
                Document document = conn.get();
                Elements elementsByClassTitle = document.getElementsByClass("contest-title");
                elementsByClassTitle.stream().map(e -> e.getElementsByTag("a").attr("href"))
                        .forEach(contestLinks::add);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        contestLinks.subList(0, 5).forEach(l -> log.info("contest link={}", l));
        return contestLinks;
    }




}
