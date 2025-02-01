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
    public Task newTask(Task task) {
        try {
            taskBaseDAO.writeTask(task);
            return task;
        } catch (Exception e) {
            LOG.error("Error creating new task", e);
            throw new RuntimeException("Error creating new task", e);
        }
    }

    @Query
    public List<Task> listAll() { //TODO[#2]: Implement the listAll() method to list all tasks.
        return taskBaseDAO.getAllTasks();
    }

    @Query
    public Task searchById(int id) { //TODO[#3]: Implement the searchById(int id) method to fetch a task by id.
        return taskBaseDAO.getById(id);
    }

    @Mutation
    @Transactional
    public Task editById(int id, Task taskFor) { //TODO[#4]: Implement the editById(int id, Task taskFor) method to update a task
        try {
            taskBaseDAO.update(id, taskFor);
            return taskFor;
        } catch (Exception e) {
            LOG.error("Error editing task", e);
            throw new RuntimeException("Error editing task", e);
        }
    }

    @Mutation
    @Transactional
    public void deleteById(int id) { //TODO[#5]: Implement the deleteById(int id) method to delete a task.
        try {
            taskBaseDAO.removeById(id);
        } catch (Exception e) {
            LOG.error("Error deleting task", e);
            throw new RuntimeException("Error deleting task", e);
        }
    }
}
