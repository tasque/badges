package org.badges.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badges.api.controller.query.NewsQueryParams;
import org.badges.api.domain.news.AchtungNewsDto;
import org.badges.api.domain.news.ActionRequiredType;
import org.badges.api.domain.news.CampaignBadgeAssignmentNews;
import org.badges.api.domain.news.NewsDto;
import org.badges.api.domain.news.UserNewsDto;
import org.badges.db.User;
import org.badges.db.repository.BadgeRepository;
import org.badges.db.repository.UserRepository;
import org.badges.service.CampaignService;
import org.badges.service.NewsService;
import org.badges.service.converter.NewsConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static java.util.Arrays.asList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
@Slf4j
public class NewsController {


    private final NewsService newsService;

    private final NewsConverter newsConverter;

    private final CampaignService campaignService;

    private final BadgeRepository badgeRepository;

    private final UserRepository userRepository;

    @GetMapping
    public Page<NewsDto> showNews(NewsQueryParams pageable) {

        return newsService.findNews(pageable)
                .map(newsConverter::shortConvert);
    }

    @GetMapping("/achtung")
    public Page<AchtungNewsDto> requiredAttentionNews(NewsQueryParams pageable) {
        User novolodskiy = userRepository.findOne(1L);
        User turovtsov = userRepository.findOne(4L);
        return new PageImpl<>(asList(
                new AchtungNewsDto().setId(123L)
                        .setActionRequired(ActionRequiredType.ASSIGN_BADGE)
                        .setEntityId(4L)
                        .setComment(badgeRepository.findOne(4L).getName())
                        .setImageUrl(badgeRepository.findOne(4L).getImageUrl()),
                new AchtungNewsDto().setId(123L)
                        .setActionRequired(ActionRequiredType.ASSIGN_BADGE)
                        .setEntityId(16L)
                        .setComment(badgeRepository.findOne(16L).getName())
                        .setImageUrl(badgeRepository.findOne(16L).getImageUrl())
                        .setToUsers(asList(new UserNewsDto().setId(novolodskiy.getId())
                                        .setImageUrl(novolodskiy.getImageUrl())
                                        .setName(novolodskiy.getName()),
                                new UserNewsDto().setId(turovtsov.getId())
                                        .setImageUrl(turovtsov.getImageUrl())
                                        .setName(turovtsov.getName()))),
                new AchtungNewsDto().setId(123L)
                        .setActionRequired(ActionRequiredType.LOOK_AT_CAMPAIGN_RESULTS)
                        .setEntityId(2L)
                        .setComment(badgeRepository.findOne(4L).getCampaign().getDescription())
                        .setImageUrl(badgeRepository.findOne(4L).getCampaign().getImageUrl())
        ));
    }

    @GetMapping("/{id}")
    public NewsDto getById(@PathVariable("id") long id) {

        return newsConverter.convert(
                newsService.news(id));
    }

    @GetMapping("/campaign/{id}")
    public Collection<CampaignBadgeAssignmentNews> getCampaignById(@PathVariable("id") long id) {

        return campaignService.news(id);
    }


}
