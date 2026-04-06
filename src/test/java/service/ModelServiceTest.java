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
import ru.computer.inventory.entity.Model;
import ru.computer.inventory.entity.ModelStructure;
import ru.computer.inventory.exception.ResourceNotFoundException;
import ru.computer.inventory.repository.ComponentRepository;
import ru.computer.inventory.repository.ModelRepository;
import ru.computer.inventory.repository.ModelStructureRepository;
import ru.computer.inventory.service.impl.ModelServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ModelServiceTest {

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private ModelStructureRepository modelStructureRepository;

    @Mock
    private ComponentRepository componentRepository;

    @InjectMocks
    private ModelServiceImpl modelService;

    private Model testModel;

    private Component testComponent;

    private ModelStructure testModelStructure;

    @BeforeEach
    public void setUp() {
        testModel = new Model();
        testModel.setId(1L);
        testModel.setName("testModel");

        testComponent = new Component();
        testComponent.setId(1L);
        testComponent.setName("testComponent");
        testComponent.setCategory("testCategory");
        testComponent.setQuantity(10);
        testComponent.setPrice(10.0);

        testModelStructure = new ModelStructure();
        testModelStructure.setId(1L);
        testModelStructure.setModel(testModel);
        testModelStructure.setComponent(testComponent);
        testModelStructure.setQuantity(1);
    }

    @Test
    public void createTest() {
        Mockito.when(modelRepository.save(testModel)).thenReturn(testModel);

        Model createdModel = modelService.create(testModel);

        Assertions.assertEquals(testModel.getName(), createdModel.getName());
        Mockito.verify(modelRepository).save(testModel);
    }

    @Test
    public void positiveReadByIdTest() {
        Mockito.when(modelRepository.findById(1L)).thenReturn(Optional.of(testModel));

        Model foundModel = modelService.readById(1L);

        Assertions.assertEquals(testModel, foundModel);
        Mockito.verify(modelRepository).findById(1L);
    }

    @Test
    public void negativeReadByIdTest() {
        Mockito.when(modelRepository.findById(100L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> modelService.readById(100L));
    }

    @Test
    public void readAllTest() {
        Mockito.when(modelRepository.findAll()).thenReturn(Collections.singletonList(testModel));

        List<Model> models = modelService.readAll();

        Assertions.assertEquals(testModel, models.getFirst());
        Mockito.verify(modelRepository).findAll();
    }

    @Test
    public void positiveUpdateTest() {
        Model newData = new Model();
        newData.setName("updatedModel");

        Mockito.when(modelRepository.existsById(1L)).thenReturn(true);
        Mockito.when(modelRepository.save(newData)).thenReturn(newData);

        Model updatedModel = modelService.update(1L, newData);

        Assertions.assertEquals(newData.getName(), updatedModel.getName());
        Mockito.verify(modelRepository).save(newData);
    }

    @Test
    public void negativeUpdateTest() {
        Mockito.when(modelRepository.existsById(100L)).thenReturn(false);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> modelService.update(100L, testModel));
    }

    @Test
    public void positiveDeleteTest() {
        Mockito.when(modelRepository.existsById(1L)).thenReturn(true);
        modelService.delete(1L);
        Mockito.verify(modelRepository).deleteById(1L);
    }

    @Test
    public void negativeDeleteTest() {
        Mockito.when(modelRepository.existsById(100L)).thenReturn(false);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> modelService.delete(100L));
    }

    @Test
    public void positiveAddComponentToModelTest() {
        Mockito.when(modelRepository.findById(1L)).thenReturn(Optional.of(testModel));
        Mockito.when(componentRepository.findById(1L)).thenReturn(Optional.of(testComponent));
        Mockito.when(modelStructureRepository.findByModelIdAndComponentId(1L, 1L)).thenReturn(null);

        modelService.addComponentToModel(1L, 1L, 1);
        Mockito.verify(modelStructureRepository).save(Mockito.any(ModelStructure.class));
    }

    @Test
    public void negativeAddComponentToModelTest() {
        Mockito.when(modelRepository.findById(1L)).thenReturn(Optional.of(testModel));
        Mockito.when(componentRepository.findById(100L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> modelService.addComponentToModel(1L, 100L, 1));
    }

    @Test
    public void positiveGetModelCompositionTest() {
        Mockito.when(modelRepository.existsById(1L)).thenReturn(true);
        Mockito.when(modelStructureRepository.findAllByModelId(1L)).thenReturn(Collections.singletonList(testModelStructure));

        List<ModelStructure> structures = modelService.getModelComposition(1L);

        Assertions.assertEquals(1, structures.size());
        Assertions.assertEquals(testModelStructure, structures.getFirst());
        Mockito.verify(modelStructureRepository).findAllByModelId(1L);
    }

    @Test
    public void negativeGetModelCompositionTest() {
        Mockito.when(modelRepository.existsById(100L)).thenReturn(false);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> modelService.getModelComposition(100L));
    }

    @Test
    public void positiveCalculateModelCost() {
        Mockito.when(modelRepository.existsById(1L)).thenReturn(true);
        Mockito.when(modelStructureRepository.findAllByModelId(1L)).thenReturn(Collections.singletonList(testModelStructure));

        Double cost = modelService.calculateModelCost(1L);

        Assertions.assertEquals(10.0, cost);
    }

    @Test
    public void negativeCalculateModelCost() {
        Mockito.when(modelRepository.existsById(100L)).thenReturn(false);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> modelService.calculateModelCost(100L));
    }
}
