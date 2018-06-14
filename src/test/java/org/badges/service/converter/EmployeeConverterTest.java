package org.badges.service.converter;

import org.badges.api.domain.news.EmployeeNewsDto;
import org.badges.db.Employee;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeConverterTest {

    @InjectMocks
    private EmployeeConverter employeeConverter;

    @Test
    public void shouldConvertEmploeeForNews() {
        // given
        Employee employee = new Employee().setId(1L)
                .setName("name")
                .setImageUrl("img url");

        // when
        EmployeeNewsDto result = employeeConverter.convert(employee);

        // then
        assertThat(result.getId(), is(1L));
        assertThat(result.getName(), is("name"));
        assertThat(result.getImageUrl(), is("img url"));
    }
}
