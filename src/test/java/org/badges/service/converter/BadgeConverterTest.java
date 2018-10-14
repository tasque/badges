package org.badges.service.converter;

import org.badges.api.domain.admin.AdminBadge;
import org.badges.db.Badge;
import org.badges.security.RequestContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class BadgeConverterTest {

    @InjectMocks
    private BadgeConverter badgeConverter;

    @Mock
    private RequestContext requestContext;

    @Test
    public void shouldConvertToAdminBadge() {
        // given
        Badge badge = new Badge()
                .setId(1L)
                .setImageUrl("badge image")
                .setVersion(3)
                .setName("badge name");


        // when
        AdminBadge result = badgeConverter.convert(badge);


        // then
        assertThat(result.getId(), is(1L));
        assertThat(result.getName(), is("badge name"));
        assertThat(result.getImageUrl(), is("badge image"));
//        assertThat(result.getVersion(), is(3));
    }
}
