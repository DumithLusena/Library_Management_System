package service.custom.impl;

import dto.PublisherDTO;
import entity.Publisher;
import exceptions.custom.PublisherException;
import org.modelmapper.ModelMapper;
import repository.custom.PublisherRepository;
import service.custom.PublisherService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PublisherServiceIMPL implements PublisherService {

    private final ModelMapper mapper;
    private final PublisherRepository publisherRepository;

    public PublisherServiceIMPL(ModelMapper mapper,PublisherRepository publisherRepository) {
        this.mapper = mapper;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public boolean add(PublisherDTO publisherDTO) throws PublisherException {
        Publisher entity = convertDTOtoEntity(publisherDTO);
        try {
            return publisherRepository.save(entity);
        } catch (SQLException e) {
            if (e instanceof SQLException){
                if (((SQLException)e).getErrorCode() == 1062){
                    throw new PublisherException("ID Already Exists - Cannot Save.", e);
                } else if (((SQLException)e).getErrorCode() == 1406) {
                    String message = ((SQLException) e).getMessage();
                    String[] er = message.split("'");
                    throw new PublisherException("Data is To Large For "+er[1]);
                }
            }
            throw new PublisherException("Error Occurred Contact Developer",e);
        }
    }

    @Override
    public boolean update(PublisherDTO publisherDTO) throws PublisherException {
        Publisher entity = convertDTOtoEntity(publisherDTO);
        try {
            boolean isUpdate = publisherRepository.update(entity);
            return isUpdate;
        } catch (SQLException e) {
            if(e instanceof SQLException){
                if (((SQLException)e).getErrorCode() == 1406) {
                    String message = ((SQLException) e).getMessage();
                    String[] er = message.split("'");
                    throw new PublisherException("Data is To Large For "+er[1]);
                }
            }
            e.printStackTrace();
            throw new PublisherException("Error Occurred Contact Developer",e);
        }
    }

    @Override
    public boolean delete(Integer id) throws PublisherException {
        try {
            boolean isDelete = publisherRepository.delete(id);
            return isDelete;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PublisherException("Error Occurred Contact Developer",e);
        }
    }

    @Override
    public Optional<PublisherDTO> search(Integer id) throws PublisherException {
        try {
            Optional<Publisher> search = publisherRepository.search(id);
            if (search.isPresent()){
                return Optional.of(convertEntitytoDTO(search.get()));
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PublisherException("Contact Developer",e);
        }
    }

    @Override
    public List<PublisherDTO> getAll() throws PublisherException {
        try {
            List<Publisher> all = publisherRepository.getAll();
            if (all.isEmpty()){
                throw new PublisherException("No Publishers Found");
            }
            List<PublisherDTO> publisherDTOS = new ArrayList<>();
            for (Publisher publisher : all) {
                PublisherDTO publisherDTO = convertEntitytoDTO(publisher);
                publisherDTOS.add(publisherDTO);
            }
            return publisherDTOS;

        } catch (SQLException e) {
            throw new PublisherException("Contact Develloper",e);
        }
    }
    
    private Publisher convertDTOtoEntity(PublisherDTO publisherDTO) {
        return mapper.map(publisherDTO, Publisher.class);
    }
    
    private PublisherDTO convertEntitytoDTO(Publisher publisherEntity) {
        return mapper.map(publisherEntity, PublisherDTO.class);
    }
}
