package com.example.app.service;

import com.example.app.dto.category.CategoryDTO;
import com.example.app.entities.Category;
import com.example.app.exception.category.CategoryException;
import com.example.app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryDTO save(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new CategoryException("This category is already exist");
        }

        return modelMapper.map(
                categoryRepository.save(Category.builder().name(name).build()),
                CategoryDTO.class
        );
    }

    public CategoryDTO update(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new CategoryException("This category(name) is already exist");
        }

        Category category = categoryRepository.findById(categoryDTO.getId())
                .orElseThrow(() -> new CategoryException("Wrong id ?"));

        category.setName(categoryDTO.getName());

        return modelMapper.map(category, CategoryDTO.class);
    }

    public void delete(String id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryException("Wrong id ?");
        }
        categoryRepository.deleteById(id);
    }

    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CategoryDTO.class))
                .collect(Collectors.toList());
    }
}
