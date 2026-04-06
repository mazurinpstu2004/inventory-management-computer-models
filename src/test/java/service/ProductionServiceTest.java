package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.computer.inventory.entity.*;
import ru.computer.inventory.exception.InsufficientStockException;
import ru.computer.inventory.exception.ResourceNotFoundException;
import ru.computer.inventory.repository.*;
import ru.computer.inventory.service.impl.ProductionServiceImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductionServiceTest {

    @Mock
    private ProductionLogRepository productionLogRepository;

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private ModelStructureRepository modelStructureRepository;

    @Mock
    private ComponentRepository componentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductionServiceImpl productionLogService;

    private User testUser;
    private Model testModel;
    private Component testComponent;
    private ModelStructure testStructure;
    private ProductionLog testLog;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUsername("testUser");

        testModel = new Model();
        testModel.setId(1L);
        testModel.setName("testModel");

        testComponent = new Component();
        testComponent.setName("testComponent");
        testComponent.setCategory("testCategory");
        testComponent.setQuantity(10);
        testComponent.setPrice(10.0);

        testStructure = new ModelStructure();
        testStructure.setComponent(testComponent);
        testStructure.setQuantity(2);

        testLog = new ProductionLog();
        testLog.setId(1L);
        testLog.setModel(testModel);
        testLog.setUser(testUser);
        testLog.setDate(LocalDateTime.now());

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        Mockito.when(auth.getName()).thenReturn(testUser.getUsername());
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void createTest() {
        Mockito.when(productionLogRepository.save(testLog)).thenReturn(testLog);

        ProductionLog createdProduction = productionLogService.create(testLog);

        Assertions.assertEquals(testLog.getId(), createdProduction.getId());
        Mockito.verify(productionLogRepository).save(testLog);
    }

    @Test
    public void positiveReadByIdTest() {
        Mockito.when(productionLogRepository.findById(1L)).thenReturn(Optional.of(testLog));

        ProductionLog foundProduction = productionLogService.readById(1L);

        Assertions.assertEquals(testLog, foundProduction);
        Mockito.verify(productionLogRepository).findById(1L);
    }

    @Test
    public void negativeReadByIdTest() {
        Mockito.when(productionLogRepository.findById(100L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productionLogService.readById(100L));
    }

    @Test
    public void readAllTest() {
        Mockito.when(productionLogRepository.findAll()).thenReturn(Collections.singletonList(testLog));

        List<ProductionLog> logs = productionLogService.readAll();

        Assertions.assertFalse(logs.isEmpty());
        Assertions.assertEquals(1, logs.size());
        Assertions.assertEquals(testLog, logs.getFirst());
    }

    @Test
    public void positiveUpdateTest() {
        ProductionLog newData = new ProductionLog();
        newData.setDate(LocalDateTime.now());

        Mockito.when(productionLogRepository.existsById(1L)).thenReturn(true);
        Mockito.when(productionLogRepository.save(newData)).thenReturn(newData);

        ProductionLog result = productionLogService.update(1L, newData);

        Assertions.assertNotNull(result.getDate());
        Mockito.verify(productionLogRepository).save(newData);
    }

    @Test
    public void negativeUpdateTest() {
        Mockito.when(productionLogRepository.existsById(100L)).thenReturn(false);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productionLogService.update(100L, testLog));
    }

    @Test
    public void positiveDeleteTest() {
        Mockito.when(productionLogRepository.existsById(1L)).thenReturn(true);
        productionLogService.delete(1L);
        Mockito.verify(productionLogRepository).deleteById(1L);
    }

    @Test
    public void negativeDeleteTest() {
        Mockito.when(productionLogRepository.existsById(100L)).thenReturn(false);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productionLogService.delete(100L));
    }

    @Test
    public void positiveRegisterAssemblyTest() {
        Mockito.when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
        Mockito.when(modelRepository.findById(1L)).thenReturn(Optional.of(testModel));
        Mockito.when(modelStructureRepository.findAllByModelId(1L)).thenReturn(List.of(testStructure));
        Mockito.when(productionLogRepository.save(Mockito.any(ProductionLog.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProductionLog result = productionLogService.registerAssembly(1L);

        Assertions.assertEquals(testModel, result.getModel());
        Assertions.assertEquals(testUser, result.getUser());
        Assertions.assertEquals(8, testComponent.getQuantity());
        Mockito.verify(productionLogRepository).save(Mockito.any(ProductionLog.class));
        Mockito.verify(componentRepository).save(testComponent);
    }

    @Test
    public void negativeRegisterAssemblyTest() {
        testComponent.setQuantity(1);

        Mockito.when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
        Mockito.when(modelRepository.findById(1L)).thenReturn(Optional.of(testModel));
        Mockito.when(modelStructureRepository.findAllByModelId(1L)).thenReturn(List.of(testStructure));

        Assertions.assertThrows(InsufficientStockException.class, () -> productionLogService.registerAssembly(1L));
    }
}