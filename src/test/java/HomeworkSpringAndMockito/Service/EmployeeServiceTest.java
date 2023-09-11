package HomeworkSpringAndMockito.Service;

import HomeworkSpringAndMockito.Exception.EmployeeAlreadyAddedException;
import HomeworkSpringAndMockito.Exception.EmployeeNotFoundException;
import HomeworkSpringAndMockito.Exception.EmployeeStorageIsFullException;
import HomeworkSpringAndMockito.Model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    EmployeeService employeeService = new EmployeeService();

    @Test
    void testAddEmployeeShouldAddedEmployeeWithItsOwnPropertiesAndWithCapitalLetter() {
        employeeService.addEmployee("test", "test2", 10_000, 1);

        Collection<Employee> allEmployees = employeeService.getAll();
        assertEquals(1, allEmployees.size());
        var employee = allEmployees.iterator().next();
        assertEquals("Test", employee.getFirstName());
        assertEquals("Test2", employee.getLastName());
        assertEquals(10_000, employee.getSalary());
        assertEquals(1, employee.getDepartment());
    }

    @Test
    void testAddEmployeesShouldReturnExceptionWhenListOfEmployeesIsFull() {
        for (int i = 0; i < 10; i++) {
            employeeService.addEmployee("test_" + i, "test_test_" + i, 0d, 0);
        }
        assertThrows(EmployeeStorageIsFullException.class, () -> employeeService.addEmployee("test", "test", 0, 0));
    }

    @Test
    void testAddEmployeesShouldReturnExceptionThatSuchAnEmployeeHasAlreadyBeenAdded() {
        employeeService.addEmployee("test", "test", 0, 0);
        assertThrows(EmployeeAlreadyAddedException.class, () -> employeeService.addEmployee("test", "test", 0, 0));
    }

    @Test
    void testFindEmployeeShouldReturnEmployeeWhichWeAdded() {
        employeeService.addEmployee("test", "testtest", 10_000, 1);
        var actual = employeeService.findEmployee("test", "testtest");
        assertEquals("Test", actual.getFirstName());
        assertEquals("Testtest", actual.getLastName());
        assertEquals(10_000, actual.getSalary());
        assertEquals(1, actual.getDepartment());
    }

    @Test
    void testFindEmployeeWhichNotExistShouldReturnException() {
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.findEmployee("test", "testtest"));
    }

    @Test
    void testRemoveEmployeeIfEmployeeAddedShouldReturnExceptionIfEmployeeNotAdded() {
        employeeService.addEmployee("test", "testtest", 10, 1);
        assertEquals(1, employeeService.getAll().size());
        assertTrue(employeeService.removeEmployee("test", "testtest"));
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.removeEmployee("not_exist", "not_exist"));
    }

    @Test
    void testGetAllShouldReturnAllEmployeesWithCorrectPropertyValues() {
        employeeService.addEmployee("test_1", "test_test_1", 100, 1);
        employeeService.addEmployee("test_2", "test_test_2", -100, 1);
        employeeService.addEmployee("test_3", "test_test_3", 100, -1);

        var all = employeeService.getAll();
        Assertions.assertThat(all.size()).isEqualTo(3);
        Assertions.assertThat(all)
                .containsExactlyInAnyOrder(
                        new Employee("Test_1", "Test_test_1", 100, 1),
                        new Employee("Test_2", "Test_test_2", -100, 1),
                        new Employee("Test_3", "Test_test_3", 100, -1));
    }
}