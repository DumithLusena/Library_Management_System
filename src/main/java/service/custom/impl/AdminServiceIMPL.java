package service.custom.impl;

import dto.AdminDTO;
import entity.Admin;
import exceptions.custom.AdminException;
import org.modelmapper.ModelMapper;
import repository.custom.AdminRepository;
import service.custom.AdminService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AdminServiceIMPL implements AdminService {

    final AdminRepository adminRepository;
    final ModelMapper mapper;


    public AdminServiceIMPL(ModelMapper mapper, AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean add(AdminDTO adminDTO) throws AdminException {
        Admin adminEntity = convertEntity(adminDTO);
        try {
            return adminRepository.save(adminEntity);
        } catch (SQLException e) {
            if (e instanceof SQLException){
                if (((SQLException)e).getErrorCode() == 1062){
                    throw new AdminException("Email Already Exists", e);
                } else if (((SQLException)e).getErrorCode() == 1406) {
                    String message = ((SQLException) e).getMessage();
                    String[] er = message.split("'");
                    throw new AdminException("Data is To Large For "+er[1]);
                }
            }
            throw new AdminException("Error Occurred Contact Developer",e);
        }
    }

    @Override
    public boolean update(AdminDTO adminDTO) throws AdminException {
        return false;
    }

    @Override
    public boolean delete(Integer integer) throws AdminException {
        return false;
    }

    @Override
    public Optional<AdminDTO> search(Integer integer) throws AdminException {
        try {
            Optional<Admin> search = adminRepository.search(integer);
            if (search.isPresent()){
                Admin admin = search.get();
                AdminDTO adminDTO = convertDTO(admin);
                return Optional.of(adminDTO);
            }
        } catch (SQLException e) {
            throw new AdminException("Contact Developer",e);
        }

        return Optional.empty();
    }

    @Override
    public List<AdminDTO> getAll() throws AdminException {
        return List.of();
    }

    @Override
    public AdminDTO login(String email, String password) {
        AdminDTO adminDTO = null;
        try {
            adminDTO = adminRepository.login(email, password);
        } catch (SQLException e) {
            System.err.println("Error occurred while logging in: " + e.getMessage());
            throw new RuntimeException("Login failed due to database error.", e);
        }

        if (adminDTO == null) {
            System.err.println("Login failed: Invalid email or password.");
        }

        return adminDTO;
    }

    private Admin convertEntity(AdminDTO adminDTO){
        return this.mapper.map(adminDTO,Admin.class);
    }

    private AdminDTO convertDTO(Admin admin){
        return this.mapper.map(admin,AdminDTO.class);
    }

}
