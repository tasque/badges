package org.badges.service;

import org.badges.api.domain.ImportBadgeAssignment;
import org.badges.db.Badge;
import org.badges.db.BadgeAssignment;
import org.badges.db.Company;
import org.badges.db.Employee;
import org.badges.db.News;
import org.badges.db.repository.BadgeAssignmentRepository;
import org.badges.db.repository.BadgeRepository;
import org.badges.db.repository.EmployeeRepository;
import org.badges.security.TenantContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BadgeAssignmentServiceTest {

    @InjectMocks
    private BadgeAssignmentService badgeAssignmentService;

    @Mock
    private NewsService newsService;

    @Mock
    private BadgeAssignmentRepository badgeAssignmentRepository;

    @Mock
    private BadgeRepository badgeRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TenantContext tenantContext;

    @Captor
    private ArgumentCaptor<BadgeAssignment> captor;

    @Test
    public void shouldSaveBadgeAssignmentWithNews() {
        // given
        when(tenantContext.getCurrentCompany()).thenReturn(new Company().setId(1L));
        ImportBadgeAssignment assignment = new ImportBadgeAssignment().setComment("comment")
                .setBadgeId(2L)
                .setAssignerId(3L)
                .addEmployees(4L, 5L);
        when(employeeRepository.findAllById(assignment.getEmployeesIds())).thenReturn(Arrays
                .asList(new Employee().setId(4L), new Employee().setId(5L)));
        when(badgeRepository.getOne(2L)).thenReturn(new Badge().setId(2L));
        when(newsService.prepareNews(captor.capture())).thenReturn(new News().setId(6L));

        // when
        News news = badgeAssignmentService.assignBadge(assignment);

        // then
        assertThat(news.getId(), is(6L));
        BadgeAssignment value = captor.getValue();
        verify(badgeAssignmentRepository).save(value);
        assertThat(value.getNews().getId(), is(6L));
        assertThat(value.getComment(), is("comment"));
        assertThat(value.getBadge().getId(), is(2L));
        assertThat(value.getToEmployees().size(), is(2));
        assertThat(value.getCompany().getId(), is(1L));
    }
}
