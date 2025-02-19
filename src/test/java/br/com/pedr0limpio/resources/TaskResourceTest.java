package br.com.pedr0limpio.resources;

import br.com.pedr0limpio.enums.Priority;
import br.com.pedr0limpio.enums.Tag;
import br.com.pedr0limpio.models.Task;
import br.com.pedr0limpio.services.TaskBaseDAO;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.*;

@QuarkusTest
public class TaskResourceTest {

    @Mock
    private TaskBaseDAO taskBaseDAO;

    @InjectMocks
    private TaskResource taskResource;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Task mockTask = new Task();
        mockTask.setDescription("create a new task");
        mockTask.setPriority(Priority.Medium);
        mockTask.setTagList(Arrays.asList(Tag.Work, Tag.Fun));
        mockTask.setCreation(new Date());
        mockTask.setConclusion(null);

        doNothing().when(taskBaseDAO).writeTask(any(Task.class));
        when(taskBaseDAO.getById(1)).thenReturn(mockTask);
    }

    @Test
    public void testNewTask() {
        Task newTask = new Task();
        newTask.setDescription("create a new task");
        newTask.setPriority(Priority.Medium);
        newTask.setTagList(Arrays.asList(Tag.Work, Tag.Fun));
        newTask.setCreation(new Date());
        newTask.setConclusion(null);

        taskResource.newTask(newTask);
        verify(taskBaseDAO).writeTask(any(Task.class));
    }
    //TODO[#6]: Make all the missing tests. Try starting here using TDD technique.
    //TODO[#7]: Check whether it will be necessary to create tests for the service classes, if so,
    // create the class(es) and the tests, if not, delete the services package from src/test.
}
