package service.custom.impl;

import dto.CategoryDTO;
import entity.Category;
import exceptions.custom.CategoryException;
import org.modelmapper.ModelMapper;
import repository.custom.CategoryRepository;
import service.custom.CategoryService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryServiceIMPL implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;
    
    public CategoryServiceIMPL(ModelMapper mapper,CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }
    
    @Override
    public boolean add(CategoryDTO categoryDTO) throws CategoryException {
        Category entity = convertDTOtoEntity(categoryDTO);
        try {
            return categoryRepository.save(entity);
        } catch (SQLException e) {
            if (e instanceof SQLException){
                if (((SQLException)e).getErrorCode() == 1062){
                    throw new CategoryException("ID Already Exists - Cannot Save.", e);
                } else if (((SQLException)e).getErrorCode() == 1406) {
                    String message = ((SQLException) e).getMessage();
                    String[] er = message.split("'");
                    throw new CategoryException("Data is To Large For "+er[1]);
                }
            }
            throw new CategoryException("Error Occurred Contact Developer",e);
        }
    }

    @Override
    public boolean update(CategoryDTO categoryDTO) throws CategoryException {
        Category entity = convertDTOtoEntity(categoryDTO);
        try {
            return categoryRepository.update(entity);
        } catch (SQLException e) {
            if (e instanceof SQLException){
                if (((SQLException)e).getErrorCode() == 1062){
                    throw new CategoryException("ID Already Exists - Cannot Save.", e);
                } else if (((SQLException)e).getErrorCode() == 1406) {
                    String message = ((SQLException) e).getMessage();
                    String[] er = message.split("'");
                    throw new CategoryException("Data is To Large For "+er[1]);
                }
            }
            throw new CategoryException("Error Occurred Contact Developer",e);
        }
    }

    @Override
    public boolean delete(Integer integer) throws CategoryException {
        try {
            return categoryRepository.delete(integer);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CategoryException("Error Occurred Contact Developer",e);
        }
    }

    @Override
    public Optional<CategoryDTO> search(Integer id) throws CategoryException {
        try {
            Optional<Category> search = categoryRepository.search(id);
            if (search.isPresent()){
                Category category = search.get();
                CategoryDTO categoryDTO = convertEntitytoDTO(category);
                return Optional.of(categoryDTO);
            }
        } catch (SQLException e) {
            throw new CategoryException("Contact Developer",e);
        }
        return Optional.empty();
    }

    @Override
    public List<CategoryDTO> getAll() throws CategoryException {
        try {
            List<Category> all = categoryRepository.getAll();
            if (all.isEmpty()){
                throw new CategoryException("No Categories Found");
            }
            ArrayList<CategoryDTO> list = new ArrayList<>();
            for (Category category : all) {
                CategoryDTO categoryDTO = convertEntitytoDTO(category);
                list.add(categoryDTO);
            }
            return list;
        } catch (SQLException e) {
            throw new CategoryException("Contact Develloper",e);
        }
    }

    private Category convertDTOtoEntity(CategoryDTO categoryDTO){
        return this.mapper.map(categoryDTO,Category.class);
    }
    
    private CategoryDTO convertEntitytoDTO(Category category){
        return this.mapper.map(category,CategoryDTO.class);
    }
}
