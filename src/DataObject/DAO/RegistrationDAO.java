package DataObject.DAO;
import Core.Entities.Registration;
import Core.Interfaces.IRegistrationDAO;
import Utilities.FileIO.IFileIO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegistrationDAO implements IRegistrationDAO{
    private static final String DEFAULT_PATH = "src/DataObject/data/Students.txt";

    private final IFileIO<Registration> fileIO;
    private final List<Registration> registrations;

    public RegistrationDAO(IFileIO<Registration> fileIO){
        this.fileIO = fileIO;
        this.registrations = loadFromFile();
    }


    private List<Registration> loadFromFile(){
        try{
            return fileIO.readFromFile();
        }catch (Exception e){
            System.out.println("Can't read registration" + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Registration> readAll(){
        return new ArrayList<>(registrations);
    }

    @Override
    public boolean writeAll(List<Registration> list){
        registrations.clear();
        registrations.addAll(list);
        return save();
    }

    @Override
    public boolean save(){
        try {
            return fileIO.saveToFile(registrations);
        }catch (Exception e){
            System.out.println("Can't save registration" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean add(Registration o){
        return registrations.add(o);
    }

    public boolean update(Registration o){
        for(int i = 0; i < registrations.size(); i++){
            if(registrations.get(i).equals(o)){
                registrations.set(i, o);
                return true;
            }
        }
        return false;
    }

    private int indexOf(String id) {
        if (id == null) return -1;
        String[] parts = id.split("\\|");
        if (parts.length != 2) return -1;
        String studentId = parts[0].trim();
        String courseId = parts[1].trim();

        for (int i = 0; i < registrations.size(); i++) {
            Registration r = registrations.get(i);
            if (r.getStudentId().getId().equalsIgnoreCase(studentId)
                    && r.getCourseId().getCourseId().equalsIgnoreCase(courseId)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean delete(String id) {
        int i = indexOf(id);
        if (i < 0) return false;
        registrations.remove(i);
        return true;
    }

    @Override
    public Optional<Registration> findByID(String id) {
        int i = indexOf(id);
        if(i >= 0){
            return Optional.of(registrations.get(i));
        }
        else{
            return Optional.empty();
        }
    }

}
