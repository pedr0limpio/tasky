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

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    public void testGetById() {
        Task mockTask = mock(Task.class);
        when(mockTask.getDescription()).thenReturn("create a new task");
        when(mockTask.getPriority()).thenReturn(Priority.Medium);
        when(mockTask.getTagList()).thenReturn(Arrays.asList(Tag.Work, Tag.Fun));
        when(taskBaseDAO.getById(1)).thenReturn(mockTask);

        Task task = taskResource.searchById(1);

        assertEquals("create a new task", task.getDescription());
        assertEquals(Priority.Medium, task.getPriority());
        assertEquals(Arrays.asList(Tag.Work, Tag.Fun), task.getTagList());
        verify(taskBaseDAO).getById(1);
    }

    @Test
    public void testGetById_NotFound() {
        when(taskBaseDAO.getById(999)).thenReturn(null);
        Task task = taskResource.searchById(999);
        assertEquals(null, task);
        verify(taskBaseDAO).getById(999);
    }

    @Test
    public void testGetById_DaoThrowsException() {
        when(taskBaseDAO.getById(2)).thenThrow(new RuntimeException("DB error"));
        try {
            taskResource.searchById(2);
        } catch (RuntimeException e) {
            assertEquals("Error fetching task by id", e.getMessage());
            verify(taskBaseDAO).getById(2);
            return;
        }
        throw new AssertionError("Expected RuntimeException was not thrown");
    }
}

    //TODO[#6]: Make all the missing tests. Try starting here using TDD technique.
    //TODO[#7]: Check whether it will be necessary to create tests for the service classes, if so, create the class(es)
