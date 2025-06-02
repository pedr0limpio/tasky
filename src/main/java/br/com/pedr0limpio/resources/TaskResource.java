package br.com.pedr0limpio.resources;
import br.com.pedr0limpio.models.Task;
import br.com.pedr0limpio.services.TaskBaseDAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.jboss.logging.Logger;
import java.util.List;

@RequestScoped
@GraphQLApi
public class TaskResource {

    private static final Logger LOG = Logger.getLogger(TaskResource.class);

    @Inject
    TaskBaseDAO taskBaseDAO;

    @Mutation
    @Transactional
    @Description("Create a new task")
    public int newTask(Task task) {
        int id = -1;
        try {
            id = taskBaseDAO.writeTask(task);
        } catch (Exception e) {
            LOG.error("Error creating new task", e);
            throw new RuntimeException("Error creating new task", e);
        }
        return id;
    }

    @Query
    public List<Task> listAll() { //TODO[#2]: Implement the listAll() method to list all tasks.
        return taskBaseDAO.getAllTasks();
    }

    @Query
    @Description("Fetch a task by id")
    public Task searchById(int id) {
        try {
            return taskBaseDAO.getById(id);
        } catch (Exception e) {
            LOG.error("Error fetching task by id", e);
            throw new RuntimeException("Error fetching task by id", e);
        }
    }

    @Mutation
    @Transactional
    public Task editById(int id, Task updatedTask) {
        try {
            taskBaseDAO.update(id, updatedTask);
            return updatedTask;
        } catch (Exception e) {
            LOG.error("Error updating task with id " + id, e);
            throw new RuntimeException("Could not update task", e);
        }
    }

    @Mutation
    @Transactional
    public String deleteById(int id) {
        try {
            Task existing = taskBaseDAO.getById(id);
            if (existing == null) {
                return "task id " + id + " not found";
            }
            taskBaseDAO.removeById(id);
            return "task id " + id + " deleted";
        } catch (Exception e) {
            LOG.error("Error deleting task", e);
            throw new RuntimeException("Error deleting task", e);
        }
    }
}
