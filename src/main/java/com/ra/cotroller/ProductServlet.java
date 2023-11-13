package com.ra.cotroller;

import com.ra.model.Product;
import com.ra.service.ProductService;
import com.ra.service.ProductServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();

    @Override
    public void init() throws ServletException {
        // Khởi tạo một số sản phẩm mẫu và thêm chúng vào danh sách sản phẩm
        Product product1 = new Product(1, "sản phẩm 1", 1111, "mô tả", "nhà sản xuất 1");
        Product product2 = new Product(2, "sản phẩm 2", 2222, "mô tả 2", "nhà sản xuất 2");

        // Thêm vào danh sách sản phẩm
        productService.addProduct(product1);
        productService.addProduct(product2);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                listProducts(request, response);
                break;
            case "view":
                viewProduct(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteProduct(request, response);
                break;
            case "search":
                searchProducts(request, response);
                break;
            default:
                listProducts(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addProduct(request, response);
        } else if ("update".equals(action)) {
            updateProduct(request, response);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productService.getAllProducts();
        request.setAttribute("products", products);
        RequestDispatcher dispatcher = request.getRequestDispatcher("views/product-list.jsp");
        dispatcher.forward(request, response);
    }

    private void viewProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("id"));
        Product product = productService.getProductById(productId);

        if (product != null) {
            request.setAttribute("product", product);
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/product-view.jsp");
            dispatcher.forward(request, response);
        } else {
            request.setAttribute("error", "Sản phẩm không tồn tại");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("views/product-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("id"));
        Product product = productService.getProductById(productId);

        if (product != null) {
            request.setAttribute("product", product);
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/product-form.jsp");
            dispatcher.forward(request, response);
        } else {
            // Xử lý sản phẩm không tồn tại
            request.setAttribute("error", "Sản phẩm không tồn tại");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("id"));
        productService.deleteProduct(productId);
        listProducts(request, response);
    }

    private void searchProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productName = request.getParameter("name");
        List<Product> products = productService.searchProductsByName(productName);
        request.setAttribute("products", products);
        RequestDispatcher dispatcher = request.getRequestDispatcher("views/product-list.jsp");
        dispatcher.forward(request, response);
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String priceString = request.getParameter("price");
        String description = request.getParameter("description");
        String manufacturer = request.getParameter("producer");

        try {
            double price = Double.parseDouble(priceString);
            Product newProduct = new Product(0, name, price, description, manufacturer);
            productService.addProduct(newProduct);

            listProducts(request, response);
        } catch (NumberFormatException e) {
            // Xử lý lỗi kiểu dữ liệu không hợp lệ
            request.setAttribute("error", "Giá sản phẩm không hợp lệ");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idString = request.getParameter("id");
        String name = request.getParameter("name");
        String priceString = request.getParameter("price");
        String description = request.getParameter("description");
        String manufacturer = request.getParameter("producer");

        try {
            int productId = Integer.parseInt(idString);
            double price = Double.parseDouble(priceString);
            Product updatedProduct = new Product(productId, name, price, description, manufacturer);
            productService.updateProduct(updatedProduct);

            listProducts(request, response);
        } catch (IllegalArgumentException e) {
            // Xử lý lỗi kiểu dữ liệu không hợp lệ hoặc sản phẩm không tồn tại
            request.setAttribute("error", "Dữ liệu không hợp lệ hoặc sản phẩm không tồn tại");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
        }
    }
}
