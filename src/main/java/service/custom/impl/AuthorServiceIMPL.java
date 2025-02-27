package service.custom.impl;

import dto.AuthorDTO;
import entity.Author;
import exceptions.custom.AuthorException;
import org.modelmapper.ModelMapper;
import repository.custom.AuthorRepository;
import service.custom.AuthorService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorServiceIMPL implements AuthorService {

    final AuthorRepository authorRepository;
    final ModelMapper mapper;

    public AuthorServiceIMPL(ModelMapper mapper,AuthorRepository authorRepository) {
        this.mapper = mapper;
        this.authorRepository = authorRepository;
    }

    @Override
    public boolean add(AuthorDTO authorDTO) throws AuthorException {
        Author entity = convertDTOtoEntity(authorDTO);
        try {
            return authorRepository.save(entity);
        } catch (SQLException e) {
            if (e instanceof SQLException){
                if (((SQLException)e).getErrorCode() == 1062){
                    throw new AuthorException("ID Already Exists - Cannot Save.", e);
                } else if (((SQLException)e).getErrorCode() == 1406) {
                    String message = ((SQLException) e).getMessage();
                    String[] er = message.split("'");
                    throw new AuthorException("Data is To Large For "+er[1]);
                }
            }
            throw new AuthorException("Error Occurred Contact Developer",e);
        }
    }

    @Override
    public boolean update(AuthorDTO authorDTO) throws AuthorException {
        Author entity = convertDTOtoEntity(authorDTO);
        try {
            boolean isUpdate = authorRepository.update(entity);
            return isUpdate;
        } catch (SQLException e) {
            if(e instanceof SQLException){
                if (((SQLException)e).getErrorCode() == 1406) {
                    String message = ((SQLException) e).getMessage();
                    String[] er = message.split("'");
                    throw new AuthorException("Data is To Large For "+er[1]);
                }
            }
            throw new AuthorException("Error Occurred Contact Developer",e);
        }
    }

    @Override
    public boolean delete(Integer id) throws AuthorException {
        try {
            return authorRepository.delete(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuthorException("Error Occurred Contact Developer",e);
        }
    }

    @Override
    public Optional<AuthorDTO> search(Integer id) throws AuthorException {
        try {
            Optional<Author> search = authorRepository.search(id);
            if (search.isPresent()){
                Author author = search.get();
                AuthorDTO authorDTO = convertEntitytoDTO(author);
                return Optional.of(authorDTO);
            }
        } catch (SQLException e) {
            throw new AuthorException("Contact Developer",e);
        }
        return Optional.empty();
    }

    @Override
    public List<AuthorDTO> getAll() throws AuthorException {
        try {
            List<Author> all = authorRepository.getAll();
            if (all.isEmpty()){
                throw new AuthorException("No Authors Found");
            }
            List<AuthorDTO> list = new ArrayList<>();
            for (Author author : all) {
                AuthorDTO authorDTO = convertEntitytoDTO(author);
                list.add(authorDTO);
            }
            return list;
        } catch (SQLException e) {
            throw new AuthorException("Contact Develloper",e);
        }
    }
    
    private Author convertDTOtoEntity(AuthorDTO authorDTO){
        return this.mapper.map(authorDTO,Author.class);
    }
    
    private AuthorDTO convertEntitytoDTO(Author author){
       return this.mapper.map(author,AuthorDTO.class);
    }
}
