# MenuAdmin (AllergenGuard) - Project Instructions

## Backend API Reference

- **GitHub Repo**: `apptolast/menus-backend` (access via `gh api repos/apptolast/menus-backend/...`)
- **Swagger UI**: https://menus-api-dev.apptolast.com/swagger-ui/index.html#/
- **API Docs (Redocly)**: https://apptolast.github.io/menus-backend/
- **OpenAPI Spec (JSON)**: https://menus-api-dev.apptolast.com/v3/api-docs
- **Base URL (dev)**: https://menus-api-dev.apptolast.com

### API Endpoint Summary

All admin endpoints require Bearer JWT. Prefix: `/api/v1`

| Module              | Endpoints                                                                                                                                                                        | Notes                                    |
|---------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------|
| Auth                | `/auth/login`, `/auth/register`, `/auth/refresh`, `/auth/register-restaurant`, `/auth/oauth2/google/callback`, `/auth/consent`                                                   | JWT-based                                |
| Admin Restaurant    | `GET/PUT /admin/restaurant`                                                                                                                                                      | Single-tenant (one restaurant per admin) |
| Admin Ingredients   | `GET/POST /admin/ingredients`, `GET/PUT/DELETE /admin/ingredients/{id}`, `GET /admin/ingredients/search?name=`, `POST /admin/ingredients/analyze-text`                           | Global catalog                           |
| Admin Recipes       | `GET/POST /admin/recipes`, `GET/PUT/DELETE /admin/recipes/{id}`, `GET /admin/recipes/{id}/allergen-breakdown`                                                                    | Per restaurant                           |
| Admin Menus         | `GET/POST /admin/menus`, `PUT/DELETE /admin/menus/{id}`, `PUT /admin/menus/{id}/publish`, `GET /admin/menus/{id}/allergen-matrix`, `GET /admin/menus/{id}/export-pdf`            | Per restaurant                           |
| Menu Sections       | `POST /admin/menus/{menuId}/sections`, `PUT/DELETE /admin/menus/{menuId}/sections/{sectionId}`                                                                                   | Nested under menu                        |
| Menu Recipes        | `POST /admin/menus/{menuId}/recipes`, `DELETE /admin/menus/{menuId}/recipes/{recipeId}`                                                                                          | Associate recipe to menu                 |
| Admin Dishes        | `GET/POST /admin/dishes`, `PUT/DELETE /admin/dishes/{id}`, `POST /admin/dishes/{id}/allergens`, `DELETE /admin/dishes/{id}/allergens/{allergenId}`                               | Per section                              |
| Admin Digital Cards | `GET/POST /admin/digital-cards`, `PUT/DELETE /admin/digital-cards/{id}`, `POST /admin/digital-cards/{id}/generate-qr`                                                            | Carta digital                            |
| Dashboard           | `GET /admin/dashboard/stats`                                                                                                                                                     | Stats aggregation                        |
| Allergens           | `GET /allergens`, `GET /allergens/{code}`                                                                                                                                        | Public, 14 EU allergens                  |
| Public              | `GET /restaurants`, `GET /restaurants/{id}`, `GET /restaurants/{restaurantId}/menu`, `GET /restaurants/{restaurantId}/sections/{sectionId}/dishes`                               | Consumer-facing                          |
| Public Cards        | `GET /public/cards/{slug}`, `GET /public/cards/{slug}/dishes`                                                                                                                    | Digital card view                        |
| Users               | `GET/PUT/DELETE /users/me/allergen-profile`, `PUT /users/me/data-rectification`, `GET /users/me/data-export`, `DELETE /users/me/data-delete`, `POST /users/me/create-restaurant` | GDPR + profile                           |

### Key API Schemas

- **IngredientRequest**: `{name, brand, supplier, allergens[], traces[], ocrRawText, notes}`
- **RecipeRequest**: `{name, description, category, price, components[], subElaboration}`
- **RecipeComponent**: `{ingredientId, subRecipeId, quantity, unit, notes, sortOrder}`
- **MenuRequest**: `{name, description, displayOrder}`
- **DishRequest**: `{name, sectionId, description, price, imageUrl, allergens[]}`
- **DigitalCardRequest**: `{menuId, slug}` (create), `{slug, customCss, active}` (update)
- **DashboardStats**:
  `{totalIngredients, activeRecipes, totalMenus, publishedMenus, totalDigitalCards, commonAllergens[]}`

## Build & Run

```bash
./gradlew compileKotlinWasmJs    # Compile check
./gradlew wasmJsTest             # Run tests
./gradlew ktlintFormat           # Format code
./gradlew wasmJsBrowserRun       # Run in browser (dev server)
```
