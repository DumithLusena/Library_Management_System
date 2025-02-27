package service.custom.impl;

import dto.FineDTO;
import entity.Fine;
import exceptions.custom.FineException;
import org.modelmapper.ModelMapper;
import repository.custom.BorrowBookRecordsRepository;
import repository.custom.FineRepository;
import service.custom.FineService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FineServiceIMPL implements FineService {

    private static final Logger LOGGER = Logger.getLogger(FineServiceIMPL.class.getName());

    private final FineRepository fineRepository;
    private final BorrowBookRecordsRepository borrowBookRecordsRepository;
    private final ModelMapper mapper;

    public FineServiceIMPL(ModelMapper mapper, FineRepository fineRepository, BorrowBookRecordsRepository borrowBookRecordsRepository) {
        this.mapper = mapper;
        this.fineRepository = fineRepository;
        this.borrowBookRecordsRepository = borrowBookRecordsRepository;
    }

    @Override
    public boolean add(FineDTO fineDTO) throws FineException {
        Fine entity = convertDTOtoEntity(fineDTO);
        try {
            return fineRepository.save(entity);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while saving fine: ", e);
            throw new FineException("Could not save fine. Please contact the developer.");
        }
    }

    @Override
    public boolean update(FineDTO fineDTO) throws FineException {
        Fine entity = convertDTOtoEntity(fineDTO);
        try {
            return fineRepository.update(entity);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while updating fine: ", e);
            throw new FineException("Could not update fine. Please contact the developer.");
        }
    }

    @Override
    public boolean delete(Integer id) throws FineException {
        try {
            return fineRepository.delete(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while deleting fine: ", e);
            throw new FineException("Could not delete fine. Please contact the developer.");
        }
    }

    @Override
    public List<FineDTO> getAll() throws FineException {
        try {
            List<Fine> fines = fineRepository.getAll();
            return fines.stream()
                    .map(fine -> mapper.map(fine, FineDTO.class))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while retrieving fines: ", e);
            throw new FineException("Could not retrieve fines. Please contact the developer.");
        }
    }

    @Override
    public Optional<FineDTO> search(Integer id) throws FineException {
        try {
            Optional<Fine> fine = fineRepository.search(id);
            return fine.map(f -> mapper.map(f, FineDTO.class));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while searching for fine: ", e);
            throw new FineException("Could not find fine. Please contact the developer.");
        }
    }

    private Fine convertDTOtoEntity(FineDTO fineDTO) {
        return mapper.map(fineDTO, Fine.class);
    }
}
