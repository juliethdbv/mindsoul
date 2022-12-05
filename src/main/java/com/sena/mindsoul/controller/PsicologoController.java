package com.sena.mindsoul.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.lowagie.text.DocumentException;
import com.sena.mindsoul.entity.Appointment;
import com.sena.mindsoul.entity.User;
import com.sena.mindsoul.entity.Record;

import com.sena.mindsoul.repository.AppointmentRepository;
import com.sena.mindsoul.repository.UserRepository;

import com.sena.mindsoul.security.SecurityUtils;
import com.sena.mindsoul.service.AppointmentService;
import com.sena.mindsoul.service.RecordService;
import com.sena.mindsoul.util.RecordExporterPdf;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@SessionAttributes("psicologo")
@RequestMapping("/psicologo")
@Controller
public class PsicologoController {

    @Autowired
    private UserRepository userRepository; // Usuario

    @Autowired
    private AppointmentService appointmentRepository; // Cita

    @Autowired 
    private AppointmentRepository appointmentRepo;

    @Autowired
    private RecordService recordRepository; // Historial

    @Autowired
    private PasswordEncoder passwordEncoder;

    // MODULO DATOS PERSONALES

    // Pagina Principal de Psicologos

    @GetMapping("")
    public String adminView(Model m) {
        String currentUser = SecurityUtils.getUserName();
        User user = userRepository.findByEmail(currentUser);
        User users = new User();
        m.addAttribute("users", users);
        m.addAttribute("user", user);
        return "psicologo/homePsicologo";
    }

    // Cambiar Contraseña

    @PostMapping("/cambiarClave")
    public String cambiarClave(@Valid User user, SessionStatus status) {

        try {
            String emailCurrentUser = SecurityUtils.getUserName();
            User userEncontrado = userRepository.findByEmail(emailCurrentUser);
            userEncontrado.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(userEncontrado);
            status.setComplete();
            return "redirect:/psicologo";

        } catch (Exception e) {

            return "psicologo/modulos/homePsicologo";
        }
    }

    // MODULO REGISTRAR CITAS

    // Listar Datos de Cita

    @GetMapping("/listarcita")
    public String listarCita(Model m) {
        String currentUser = SecurityUtils.getUserName();
        User user = userRepository.findByEmail(currentUser);
        m.addAttribute("user", user);
        m.addAttribute("citas", appointmentRepository.findAll());

        return "psicologo/modulos/listarcita";
    }

    // Mostrar Formulario para nueva Cita

    @GetMapping("/formcita")
    public String formCita(Model m) {

        Appointment appointment = new Appointment();

        m.addAttribute("citas", appointment);
        m.addAttribute("accion", "Agregar Cita");

        return "psicologo/modulos/formcita";
    }

    // Registrar y guardar Datos de una nueva cita

    @PostMapping("/addcita")
    public String addCita(@Valid Appointment appointment, BindingResult reslt, Model m, SessionStatus status) {
        if (reslt.hasErrors()) {
            return "psicologo/modulos/formcita";
        }
        try {
            String emailCurrentUser = SecurityUtils.getUserName();
            User user = userRepository.findByEmail(emailCurrentUser);
            appointment.setUsers(user);
            appointmentRepository.save(appointment);
            status.setComplete();
            return "redirect:/psicologo/listarcita";

        } catch (Exception e) {
            // duplicate primary key
            m.addAttribute("appointments", appointment);
            m.addAttribute("error", "La cita ya existe en su disponibilidad, intente con otros datos");
            System.out.println("ya existe");
            return "psicologo/modulos/formcita";
        }
    }

    // Traer Datos al Formulario por Id para proceder a actualizar

    // Registrar / Actualizar agenda
    @GetMapping("/formagenda")
    public String formAgenda(Model m) {
        Appointment appointment = new Appointment();
        String currentUser = SecurityUtils.getUserName();
        User user = userRepository.findByEmail(currentUser);
        m.addAttribute("user", user);
        m.addAttribute("citas", appointment);
        m.addAttribute("accion", "Agregar Cita");

        return "psicologo/modulos/formagenda";
    }

    // Registrar y guardar Datos de una nueva agenda disponible

    @PostMapping("/addagenda")
    public String addAgenda(@Valid Appointment appointment, BindingResult reslt, Model m, SessionStatus status) {
        if (reslt.hasErrors()) {
            return "psicologo/modulos/formagenda";
        }
        try {
            String emailCurrentUser = SecurityUtils.getUserName();
            User user = userRepository.findByEmail(emailCurrentUser);
            appointment.setUsers(user);
            appointment.setStateAppointment(false);
            appointmentRepository.save(appointment);
            status.setComplete();
            return "redirect:/psicologo/listaragenda";

        } catch (Exception e) {
            // duplicate primary key
            m.addAttribute("appointments", appointment);
            m.addAttribute("error", "La cita ya existe en su disponibilidad, intente con otros datos");
            System.out.println("ya existe");
            return "psicologo/modulos/formagenda";
        }
    }

    // Cambiar estado Cita

    @GetMapping("/cambiarEstado/{id}")
    public String cambiarEstado(@PathVariable Long id) {
        Appointment appointment = appointmentRepository.findOne(id);

        appointment.setStateAppointment(!appointment.getStateAppointment());

        appointmentRepository.save(appointment);
        return "redirect:/psicologo/listarcita";
    }

    // MODULO VER HISTORIALES

    // listar de historial medico
    @GetMapping("/listarh")
    public String listarH(Model m) {

        m.addAttribute("historiales", recordRepository.findAll());

        return "psicologo/modulos/listarh";
    }

    // Generar PDF

    @GetMapping("/pdfgenerate")
    public void generatePDF(HttpServletResponse response) throws DocumentException,IOException{
        response.setContentType("application/pdf");	
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerkey = "content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime +  ".pdf";
        response.setHeader(headerkey,headerValue);

        List<Record> records = recordRepository.findAll();

        RecordExporterPdf exporterPDF = new RecordExporterPdf(records);
        exporterPDF.exportar(response);
    }

    @GetMapping("/listaragenda")
    public String listarAg(Model m) {

        m.addAttribute("citas", appointmentRepository.findAll());

        return "psicologo/modulos/listaragenda";
    }

    // Mostrar Formulario para nuevo Historial

    @GetMapping("/formh")
    public String formH(Model m) {
        String emailCurrentUser = SecurityUtils.getUserName();
        User user = userRepository.findByEmail(emailCurrentUser);
        List<Appointment> appointment = appointmentRepo.findAppointmentByDoctorName(user.getName());
        Record record = new Record();

        m.addAttribute("appointments", appointment);
        m.addAttribute("historiales", record);
        m.addAttribute("accion", "Agregar Historial");

        return "psicologo/modulos/formh";
    }

    // Registrar y guardar Datos de una nueva cita

    @PostMapping("/addh")
    public String addH(@Valid Record record, BindingResult reslt, Model m, SessionStatus status) {
        if (reslt.hasErrors()) {
            return "psicologo/modulos/formh";
        }
        try {
            recordRepository.save(record);
            status.setComplete();
            return "redirect:/psicologo/listarh";

        } catch (Exception e) {
            // duplicate primary key
            m.addAttribute("historiales", record);
            m.addAttribute("error", "El historial ya existe en la base de datos");
            System.out.println("ya existe");
            return "psicologo/modulos/formh";
        }
    }

    // Ver Citas
    @GetMapping("/vercita/{id}")
    public String verCita(@PathVariable Long id, Model m) {
        m.addAttribute("citas", appointmentRepository.findOne(id));
        return "/psicologo/modulos/vercita";
    }

    @GetMapping("/deleteAg/{id}")
    private String deleteAg(Model m, @PathVariable Long id) {
        if (id > 0) {
            appointmentRepository.delete(id);
        }
        return "redirect:/psicologo/listaragenda";
    }

    // Modificar Estado de Cita (Cancelado / Pendiente)
    
    @GetMapping("/modE/{id}")
    public String modE(Model m, @PathVariable Long id, SessionStatus status){   
        Appointment  a = appointmentRepository.findOne(id);
        try{
            a.setStateCancel(true);
            appointmentRepository.save(a);
            status.setComplete();
            m.addAttribute("appointments", a);
            return "redirect:/psicologo/listarcita";
        }
        catch (Exception e) {
            // duplicate primary key
            m.addAttribute("appointments", a);
            m.addAttribute("error", "No se pudo cancelar la cita.");
            System.out.println("ya existe");
            return "redirect:/psicologo/formMotivo";
        }
    }

    @GetMapping("/formMotivo/{id}")
    public String formMotivo(Model m, @PathVariable Long id) {
        Appointment a = appointmentRepository.findOne(id);
        m.addAttribute("appointments", a);
        return "psicologo/modulos/formMotivo";

    }

}
