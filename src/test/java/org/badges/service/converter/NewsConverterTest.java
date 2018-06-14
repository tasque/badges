package org.badges.service.converter;

import com.google.common.collect.Sets;
import org.badges.api.domain.news.EmployeeNewsDto;
import org.badges.api.domain.news.NewsDto;
import org.badges.db.Employee;
import org.badges.db.News;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NewsConverterTest {

    @InjectMocks
    private NewsConverter newsConverter;

    @Mock
    private EmployeeConverter employeeConverter;

    @Test
    public void shouldConvertNews() {
        // given
        Employee employee1 = new Employee().setId(1L);
        Employee employee2 = new Employee().setId(2L);
        Employee employee3 = new Employee().setId(3L);
        News news = new News()
                .setComment("comment")
                .setAuthor(employee1)
                .setToEmployees(Sets.newHashSet(employee2, employee3));
        EmployeeNewsDto empDto1 = new EmployeeNewsDto().setId(1L);
        EmployeeNewsDto empDto2 = new EmployeeNewsDto().setId(2L);
        EmployeeNewsDto empDto3 = new EmployeeNewsDto().setId(3L);
        when(employeeConverter.convert(employee1)).thenReturn(empDto1);
        when(employeeConverter.convert(employee2)).thenReturn(empDto2);
        when(employeeConverter.convert(employee3)).thenReturn(empDto3);

        // when
        NewsDto result = newsConverter.convert(news);

        // then
        assertThat(result.getComment(), is("comment"));
        assertThat(result.getAuthor(), is(empDto1));
        assertThat(result.getToEmployees(), containsInAnyOrder(empDto2, empDto3));
    }
}
