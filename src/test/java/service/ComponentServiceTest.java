package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.computer.inventory.entity.Component;
import ru.computer.inventory.exception.ResourceNotFoundException;
import ru.computer.inventory.repository.ComponentRepository;
import ru.computer.inventory.service.impl.ComponentServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ComponentServiceTest {

    @Mock
    private ComponentRepository componentRepository;

    @InjectMocks
    private ComponentServiceImpl componentService;

    private Component testComponent;

    @BeforeEach
    public void setUp() {
        testComponent = new Component();
        testComponent.setId(1L);
        testComponent.setName("testComponent");
        testComponent.setCategory("testCategory");
        testComponent.setQuantity(10);
        testComponent.setPrice(10.0);
    }

    @Test
    public void createTest() {
        Mockito.when(componentRepository.save(testComponent)).thenReturn(testComponent);

        Component createdComponent = componentService.create(testComponent);

        Assertions.assertEquals(testComponent.getName(), createdComponent.getName());
        Mockito.verify(componentRepository).save(testComponent);
    }

    @Test
    public void positiveReadByIdTest() {
        Mockito.when(componentRepository.findById(1L)).thenReturn(Optional.ofNullable(testComponent));

        Component foundComponent = componentService.readById(1L);

        Assertions.assertEquals(testComponent, foundComponent);
        Mockito.verify(componentRepository).findById(1L);
    }

    @Test
    public void negativeReadByIdTest() {
        Mockito.when(componentRepository.findById(100L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> componentService.readById(100L));
    }

    @Test
    public void readAllTest() {
        Mockito.when(componentRepository.findAll()).thenReturn(Collections.singletonList(testComponent));

        List<Component> components = componentService.readAll();

        Assertions.assertEquals(testComponent, components.getFirst());
        Mockito.verify(componentRepository).findAll();
    }

    @Test
    public void positiveUpdateTest() {
        Component newData = new Component();
        newData.setName("updatedComponent");

        Mockito.when(componentRepository.existsById(1L)).thenReturn(true);
        Mockito.when(componentRepository.save(newData)).thenReturn(newData);

        Component updatedComponent = componentService.update(1L, newData);

        Assertions.assertEquals(newData.getName(), updatedComponent.getName());
        Mockito.verify(componentRepository).save(updatedComponent);
    }

    @Test
    public void negativeUpdateTest() {
        Mockito.when(componentRepository.existsById(100L)).thenReturn(false);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> componentService.update(100L, testComponent));
    }

    @Test
    public void positiveDeleteTest() {
        Mockito.when(componentRepository.existsById(1L)).thenReturn(true);
        componentService.delete(1L);
        Mockito.verify(componentRepository).deleteById(1L);
    }

    @Test
    public void negativeDeleteTest() {
        Mockito.when(componentRepository.existsById(100L)).thenReturn(false);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> componentService.delete(100L));
    }

    @Test
    public void addStockTest() {
        Mockito.when(componentRepository.findById(1L)).thenReturn(Optional.ofNullable(testComponent));
        Mockito.when(componentRepository.save(testComponent)).thenReturn(testComponent);

        componentService.addStock(1L, 5);

        Assertions.assertEquals(15, testComponent.getQuantity());
        Mockito.verify(componentRepository).save(testComponent);
    }

    @Test
    public void searchComponentsTest() {
        List<Component> components = Collections.singletonList(testComponent);

        Mockito.when(componentRepository.findByFilters("testComponent", "testCategory", 5.0, 15.0))
                .thenReturn(components);

        List<Component> foundComponents = componentService.searchComponents("testComponent", "testCategory", 5.0, 15.0);

        Assertions.assertEquals(1L, foundComponents.size());
        Assertions.assertEquals(testComponent, foundComponents.getFirst());
        Mockito.verify(componentRepository).findByFilters("testComponent", "testCategory", 5.0, 15.0);
    }
}
