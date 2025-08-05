package com.dailycodework.fisrtprojectspringboot.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dailycodework.fisrtprojectspringboot.exceptions.AlreadyExistsException;
import com.dailycodework.fisrtprojectspringboot.exceptions.ResourceNotFoundException;
import com.dailycodework.fisrtprojectspringboot.model.Category;
import com.dailycodework.fisrtprojectspringboot.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service // Déclare cette classe comme un service Spring (géré par le framework)
@RequiredArgsConstructor // Génère un constructeur avec les champs 'final'
public class CategoryService implements ICategoryService {// Implémente le contrat
    // Dépendances injectées automatiquement par Spring via le constructeur généré
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Ajoute une nouvelle catégorie après vérification de son unicité.
     * - Vérifie qu'aucune catégorie avec le même nom n'existe déjà
     * - Persiste la nouvelle catégorie si le nom est disponible
     * - Lance une exception si le nom existe déjà
     * 
     * @param category La catégorie à créer (doit contenir un nom non null)
     * @return La catégorie nouvellement créée (avec son ID généré)
     * @throws AlreadyExistsException si une catégorie avec ce nom existe déjà
     */
    @Override
    public Category addCategory(Category category) {
        return Optional.of(category) //  Encapsule la catégorie dans un Optional
                .filter(c -> !categoryRepository.existsByName(c.getName())) //  Filtre si le nom exist
                .map(categoryRepository::save)//  Sauvegarde si le filtre passe
                .orElseThrow(() -> new AlreadyExistsException(category.getName() + "already exists"));//  Gère l'échec Message d'erreur explicite
    }

    /**
     * Met à jour une catégorie existante en suivant une approche fonctionnelle avec
     * Optional.
     * 
     * @param category Les nouvelles données de la catégorie (doit contenir au moins
     *                 le nouveau nom)
     * @param id       L'identifiant de la catégorie à modifier
     * @return La catégorie mise à jour
     * @throws ResourceNotFoundException si la catégorie n'existe pas
     */
    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id)) // Recherche la catégorie existante
                .map(oldCategory -> { // Si trouvée, exécute la mise à jour
                    oldCategory.setName(category.getName());// Met à jour uniquement le nom
                    return categoryRepository.save(oldCategory);// Persiste les modifications
                }).orElseThrow(() -> new ResourceNotFoundException("null"));// Gère le cas non trouvé
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
            throw new ResourceNotFoundException("Category not found");
        });
    }

}
