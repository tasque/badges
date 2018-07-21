package org.badges.service.converter;

import org.badges.api.domain.news.UserNewsDto;
import org.badges.db.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class UserConverterTest {

    @InjectMocks
    private UserConverter userConverter;

    @Test
    public void shouldConvertUserForNews() {
        // given
        User user = new User().setId(1L)
                .setName("name")
                .setImageUrl("img url");

        // when
        UserNewsDto result = userConverter.convertNews(user);

        // then
        assertThat(result.getId(), is(1L));
        assertThat(result.getName(), is("name"));
        assertThat(result.getImageUrl(), is("img url"));
    }
}
