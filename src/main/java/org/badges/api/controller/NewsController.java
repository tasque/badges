package org.badges.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badges.api.controller.query.NewsQueryParams;
import org.badges.api.domain.news.AchtungNewsDto;
import org.badges.api.domain.news.ActionRequiredType;
import org.badges.api.domain.news.CampaignBadgeAssignmentNews;
import org.badges.api.domain.news.NewsDto;
import org.badges.db.Badge;
import org.badges.db.User;
import org.badges.db.repository.UserRepository;
import org.badges.security.RequestContext;
import org.badges.service.BadgeService;
import org.badges.service.CampaignService;
import org.badges.service.NewsService;
import org.badges.service.converter.NewsConverter;
import org.badges.service.converter.UserConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
@Slf4j
public class NewsController {


    private final NewsService newsService;

    private final NewsConverter newsConverter;

    private final UserConverter userConverter;

    private final CampaignService campaignService;

    private final BadgeService badgeService;

    private final UserRepository userRepository;

    private final RequestContext requestContext;


    @GetMapping
    public Page<NewsDto> showNews(NewsQueryParams pageable) {

        return newsService.findNews(pageable)
                .map(newsConverter::shortConvert);
    }

    @GetMapping("/achtung")
    public Page<AchtungNewsDto> requiredAttentionNews(NewsQueryParams pageable) {
        List<AchtungNewsDto> result = new ArrayList<>();
        Long currentUserId = requestContext.getCurrentUserId();

        List<Badge> catalogue = badgeService.badgesForCatalogue(currentUserId);

        catalogue.stream()
                .filter(b -> b.getCampaign() != null)
                .map(b -> new AchtungNewsDto()
                        .setEntityId(b.getId())
                        .setImageUrl(b.getImageUrl())
                        .setActionRequired(ActionRequiredType.ASSIGN_BADGE)
                        .setComment(b.getName()))
                .forEach(result::add);


        Optional<Badge> happyBirthday = catalogue.stream()
                .filter(b -> b.getName().contains("Happy Birthday"))
                .findFirst();

        if (happyBirthday.isPresent()) {
            Badge badge = happyBirthday.get();
            List<User> toUsers = userRepository.findUsersWithoutHappyBirthday(badge.getId(), currentUserId);
            if (!toUsers.isEmpty()) {
                result.add(new AchtungNewsDto()
                        .setActionRequired(ActionRequiredType.ASSIGN_BADGE)
                        .setEntityId(badge.getId())
                        .setComment(badge.getName())
                        .setImageUrl(badge.getImageUrl())
                        .setToUsers(toUsers.stream().map(userConverter::convertForNews).collect(Collectors.toList())));
            }
        }

        result.addAll(campaignService.getRecentCampaigns());


        return new PageImpl<>(result);
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
