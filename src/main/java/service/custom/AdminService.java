package service.custom;

import dto.AdminDTO;
import service.CrudService;

public interface AdminService extends CrudService<AdminDTO,Integer> {
    AdminDTO login(String email, String password);
}
