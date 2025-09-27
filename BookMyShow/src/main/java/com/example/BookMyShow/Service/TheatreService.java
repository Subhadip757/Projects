package com.example.BookMyShow.Service;

import com.example.BookMyShow.DTO.TheatreDTO;
import com.example.BookMyShow.Exception.ResourceNotFound;
import com.example.BookMyShow.Model.Theatre;
import com.example.BookMyShow.Repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheatreService {
    @Autowired
    private TheatreRepository theatreRepository;

    public TheatreDTO createTheatre(TheatreDTO theatreDTO){
        Theatre theatre = mapToEntity(theatreDTO);
        Theatre savedTheatre = theatreRepository.save(theatre);
        return mapToDto(savedTheatre);
    }

    public TheatreDTO getTheatreById(Long id){
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Theatre Not found with id: " + id));

        return mapToDto(theatre);
    }

    public List<TheatreDTO> getAllTheatres(Long id){
        List<Theatre> theatres = theatreRepository.findAll();

        return theatres.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TheatreDTO> getTheatreByCity(String city){
        List<Theatre> theatres = theatreRepository.findByCity(city);

        return theatres.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Update
    public TheatreDTO updateTheatre(TheatreDTO newTheatreDto, Long id){
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Theatre not found with id " + id));

        theatre.setName(newTheatreDto.getName());
        theatre.setAddress(newTheatreDto.getAddress());
        theatre.setCity(newTheatreDto.getCity());
        theatre.setTotalScreen(newTheatreDto.getTotalScreens());

        Theatre updateTheatre = theatreRepository.save(theatre);
        return mapToDto(updateTheatre);
    }

    // Delete
    public void deleteTheatre(Long id){
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Theatre not found with id " + id));

        theatreRepository.delete(theatre);
//        return mapToDto(theatre);
    }

    private Theatre mapToEntity(TheatreDTO theatreDTO){
        Theatre theatre = new Theatre();
        theatre.setId(theatreDTO.getId());
        theatre.setName(theatreDTO.getName());
        theatre.setCity(theatreDTO.getCity());
        theatre.setAddress(theatreDTO.getAddress());
        theatre.setTotalScreen(theatreDTO.getTotalScreens());

        return theatre;
    }

    private TheatreDTO mapToDto(Theatre theatre){
        TheatreDTO theatreDTO = new TheatreDTO();
        theatreDTO.setId(theatre.getId());
        theatreDTO.setName(theatre.getName());
        theatreDTO.setCity(theatre.getCity());
        theatreDTO.setAddress(theatre.getAddress());
        theatreDTO.setTotalScreens(theatre.getTotalScreen());

        return theatreDTO;
    }

}
