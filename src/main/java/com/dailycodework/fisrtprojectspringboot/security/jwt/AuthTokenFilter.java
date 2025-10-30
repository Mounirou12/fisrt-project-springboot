package com.dailycodework.fisrtprojectspringboot.security.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dailycodework.fisrtprojectspringboot.security.user.ShopUserDetailsService;

import io.jsonwebtoken.JwtException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component// Indique que cette classe est un composant Spring
@RequiredArgsConstructor// Injection de dépendance
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;// Injection de dépendance et oubli du keyword final
    private final ShopUserDetailsService userDetailsService;

    /**
 * Méthode principale du filtre qui intercepte chaque requête HTTP
 * pour valider l'authentification JWT
 */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,  // La requête HTTP entrante
            @NonNull HttpServletResponse response, // La réponse HTTP à renvoyer
            @NonNull FilterChain filterChain) throws ServletException, IOException {// Chaîne de filtres pour continuer le traitement
        try {
            String jwt = parseJwt(request); // Étape 1: Extraction du token JWT depuis l'en-tête Authorization
            if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {// Étape 2: Vérification si le token existe et est valide
                String username = jwtUtils.getUserNameFromToken(jwt);// Étape 3: Extraction du username depuis le token JWT
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);// Étape 4: Chargement des détails de l'utilisateur depuis la base de données
                // Étape 5: Création de l'objet d'authentification Spring Security
                var auth = new UsernamePasswordAuthenticationToken(
                    userDetails,// Principal (utilisateur authentifié)
                    null,// Credentials (mis à null par sécurité)
                    userDetails.getAuthorities());// Rôles et permissions de l'utilisateur
                // Étape 6: Stockage de l'authentification dans le contexte de sécurité
                // Cela marque l'utilisateur comme authentifié pour toute la requête
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtException e) {
            // Cas d'erreur spécifique au JWT (token expiré, signature invalide, etc.)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage() + " : Invalid or expired token, you may login and try again!");
            return;// Arrête le traitement de la requête
        } catch (Exception e) {
            // Cas d'erreur générale (base de données, service indisponible, etc.)
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            return;// Arrête le traitement de la requête
        }
        // Étape 7: Si tout est OK, continue le traitement vers le prochain filtre/controller
        filterChain.doFilter(request, response);
    }

    /**
     * Extrait le token JWT depuis l'en-tête Authorization de la requête HTTP
     * @param request La requête HTTP entrante
     * @return Le token JWT sans le préfixe "Bearer ", ou null si non trouvé
     */
    private String parseJwt(HttpServletRequest request) {
        // Récupère la valeur de l'en-tête "Authorization" de la requête
        // Exemple: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        String headerAuth = request.getHeader("Authorization");
        // Vérifie si le header existe, n'est pas vide et commence par "Bearer "
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
        // Extrait le token JWT en supprimant les 7 premiers caractères ("Bearer ")
        // Retourne uniquement la partie token: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            return headerAuth.substring(7);
        }
        // Retourne null si aucun token JWT valide n'est trouvé
        // Cas où: header absent, vide, ou format incorrect
        return null;
    }

}
