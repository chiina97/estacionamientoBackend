package sem.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import sem.controller.PatentController;
import sem.controller.UserController;
import sem.enums.RoleName;
import sem.model.City;
import sem.model.Holiday;
import sem.model.Role;
import sem.serviceImpl.CityService;
import sem.serviceImpl.HolidayService;
import sem.serviceImpl.RoleService;

/**
 * MUY IMPORTANTE: ESTA CLASE SÓLO SE EJECUTARÁ UNA VEZ PARA LOS DATOS DEL
 * SISTEMA
 * UNA VEZ CREADOS SE DEBERÁ ELIMINAR O COMENTAR EL CÓDIGO
 *
 */

@Component
public class CreateData implements CommandLineRunner {

  @Autowired
  RoleService roleService;
  @Autowired
  CityService cityService;
  @Autowired
  UserController userController;
  @Autowired
  PatentController patentController;
  @Autowired
  HolidayService holidayService;

  @Override
  public void run(String... args) throws Exception {

    // creo los roles:

	if(roleService.findAll().isEmpty()) {
		Role roleUser = new Role(RoleName.ROLE_USER);
	    roleService.save(roleUser);
	}
      

    //si no tiene una ciudad creo la city
    List<City> listcitys = (List<City>) cityService.findAll();
    if (listcitys.isEmpty()) {
      City city = new City("8:00", "12:00", 10);
      cityService.save(city);

    }

    // si no esta cargado la lista de feriado la carga:
    String listHolidays = "01/01,28/02,01/03,24/03,02/04,15/04,01/05,25/05,20/06,09/07,08/12,25/12,17/06,15/08,10/10,20/11,07/10,21/11,09/12";
    ArrayList<Holiday> listH=holidayService.findAll(); 
    if (listH.isEmpty()) {
      String[] list = listHolidays.split((","));
      for (String elem : list) {
         holidayService.save(new Holiday(elem));
      }
    }
    
    

  }
}
