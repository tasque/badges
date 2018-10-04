package org.badges.api.controller;

import org.badges.api.domain.catalog.CatalogBadge;
import org.badges.db.Badge;
import org.badges.db.campaign.BadgeCampaignRule;
import org.badges.security.RequestContext;
import org.badges.service.BadgeAssignmentService;
import org.badges.service.BadgeService;
import org.badges.service.converter.BadgeConverter;
import org.badges.service.converter.NewsConverter;
import org.badges.service.event.NotificationService;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BadgesControllerTest {

    @InjectMocks
    private BadgesController badgesController;

    @Mock
    private BadgeAssignmentService badgeAssignmentService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private NewsConverter newsConverter;

    @Mock
    private BadgeService badgeService;

    @Mock
    private BadgeConverter badgeConverter;

    @Mock
    private RequestContext requestContext;

    @Test
    public void shouldCorrectlySortBadges() {
        // given
        when(requestContext.getCurrentUserId()).thenReturn(1L);
        Badge badge1 = new Badge().setId(2L).setCategory("cat3");
        Badge badge2 = new Badge().setId(3L).setCategory("cat2");
        Badge badge3 = new Badge().setId(4L).setCategory("cat1").setBadgeCampaignRule(new BadgeCampaignRule());
        Badge badge4 = new Badge().setId(5L).setCategory("cat2");
        when(badgeService.badgesForCatalogue(1L)).thenReturn(Arrays.asList(badge1, badge2, badge3, badge4));
        when(badgeConverter.catalogBadge(badge1, 1L)).thenReturn(new CatalogBadge().setId(2));
        when(badgeConverter.catalogBadge(badge2, 1L)).thenReturn(new CatalogBadge().setId(3));
        when(badgeConverter.catalogBadge(badge3, 1L)).thenReturn(new CatalogBadge().setId(4));
        when(badgeConverter.catalogBadge(badge4, 1L)).thenReturn(new CatalogBadge().setId(5));


        // when
        Map<String, List<CatalogBadge>> catalog = badgesController.catalog();

        // then
        List<String> keys = new ArrayList<>(catalog.keySet());
        assertThat(keys.get(0), is("cat1"));
        assertThat(keys.get(1), is("cat3"));
        assertThat(keys.get(2), is("cat2"));
        assertThat(catalog.get("cat1").get(0).getId(), is(4L));
        assertThat(catalog.get("cat3").get(0).getId(), is(2L));
        assertThat(catalog.get("cat2").get(0).getId(), is(3L));
        assertThat(catalog.get("cat2").get(1).getId(), is(5L));
    }
}
