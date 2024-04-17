//Danh sách id cartDetail được chọn để checkout
var listProductIdChoice = [];

// Xử lý sự kiện khi checkbox thay đổi trạng thái
$('.cart-checkbox').change(function () {
    var cartDetailId = $(this).val(); // Lấy ID của cartDetail từ giá trị của checkbox

    // Kiểm tra xem checkbox có được chọn hay không
    if ($(this).is(':checked')) {
        // Nếu được chọn, thêm ID vào listProductIdChoice (nếu chưa có)
        if (!listProductIdChoice.includes(cartDetailId)) {
            listProductIdChoice.push(cartDetailId);
        }
    } else {
        // Nếu không được chọn, loại bỏ ID khỏi listProductIdChoice (nếu có)
        var index = listProductIdChoice.indexOf(cartDetailId);
        if (index !== -1) {
            listProductIdChoice.splice(index, 1);
        }
    }

    console.log('Danh sách được chọn:', listProductIdChoice);
});

function saveListProductToCheckout() {
    if (listProductIdChoice.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng chọn sản phẩm!'
        });
        return;
    } else {
        var listProduct = JSON.parse(localStorage.getItem("listProductIdChoice")) || {};

        listProduct.listProductIdChoice = listProductIdChoice;

        // Lưu lại dữ liệu vào Local Storage
        localStorage.setItem("listProductIdChoice", JSON.stringify(listProduct));

        window.location.href = "/checkout";
    }
}

$(document).ready(function () {
    $('.cart-checkbox').change(function () {
        calculateTotal();
    });
});


function calculateTotal() {
    var selectedIds = [];
    $('.cart-checkbox:checked').each(function () {
        selectedIds.push($(this).val());
    });

    $.ajax({
        type: 'POST',
        url: '/sumPrice',
        contentType: 'application/json',
        data: JSON.stringify(selectedIds),
        success: function (response) {
            var formattedTongTien = formatCurrency(response.totalPrice);
            $('#sumPrice').text(formattedTongTien);
        },
        error: function (xhr, status, error) {
            console.error(xhr.responseText);
        }
    });
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(amount);
}

async function decrementQuantity(productId) {
    var quantity = parseInt($("#quantity_" + productId).val());
    if (quantity > 1) {
        // Gửi yêu cầu AJAX
        await $.ajax({
            type: "POST",
            url: "/cart-detail/decrement/" + productId,
            contentType: "application/json",
            success: function (response) {
                console.log("Giảm số lượng thành công! (" + productId + " - 1)");
                $("#quantity_" + productId).val(quantity - 1);
                calculateTotal();
            },
            error: function (error) {
                console.error("Lỗi khi giảm số lượng:", error);
            }
        });
    }
}

async function incrementQuantity(productId) {
    var quantity = parseInt($("#quantity_" + productId).val());
    var maxQuantity = parseInt($('#quantity_' + productId).attr('max'));
    if (quantity < maxQuantity) {
        // Gửi yêu cầu AJAX
        await $.ajax({
            type: "POST",
            url: "/cart-detail/increment/" + productId,
            contentType: "application/json",
            success: function (response) {
                console.log("Tăng số lượng thành công! (" + productId + " + 1)");
                $("#quantity_" + productId).val(quantity + 1);
                calculateTotal();
            },
            error: function (error) {
                console.error("Lỗi khi tăng số lượng:", error);
            }
        });
    } else {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vượt số lượng tồn sản phẩm!'
        });
        return;
    }
}

