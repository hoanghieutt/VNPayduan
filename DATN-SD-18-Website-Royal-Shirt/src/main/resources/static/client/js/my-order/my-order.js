
// Đóng modal
$(document).ready(function(){
    $('#closeModal').click(function(){
        $('#showModal').modal('hide');
        // Reload trang sau khi thành công
        location.reload();
    });
});

// chuyển  tiền về 1.000 vnd
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(amount);
}

// Hàm lấy ra list order-detail
function getOrderDetails(button) {
    var orderId = button.getAttribute("data-id");
    $.ajax({
        type: "GET",
        url: "/rest/my-order/order-detail/" + orderId,
        success: function(data) {
            console.log(data);
            $('#orderDetailBody').empty();
            $.each(data, function(index, od) {
                var row = '<tr>' +
                    '<td>' + (index + 1) + '</td>';
                if (od.productImage && od.productName) {
                    row += '<td><img src="' + od.productImage + '" alt="Product Image" class="img-thumbnail" style="width: 50px; height: 50px;" onclick="redirectToProductPage(' + od.productId + ')"></td>' + // Thêm hàm onclick
                        '<td>' + od.productName + '</td>' +
                        '<td>' + od.colorName + '</td>' +
                        '<td>' + od.sizeName + '</td>';
                } else {
                    row += '<td></td>' +
                        '<td></td>';
                }
                row += '<td>' + formatCurrency(od.price) + '</td>' +
                    '<td>' + od.quantity + '</td>' +
                    '<td>' + formatCurrency(od.price * od.quantity) + '</td>' +
                    '</tr>';
                $('#orderDetailBody').append(row);
            });

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error("AJAX Error:", textStatus, errorThrown);
            alert('Có lỗi xảy ra khi lấy chi tiết đơn hàng.');
        }
    });

    // Tính tổng tiền
    $.ajax({
        type: 'GET',
        url: '/rest/my-order/totalPrice/' + orderId,
        contentType: 'application/json',
        success: function (response) {
            var formattedTongTien = formatCurrency(response);
            $('#totalPrice').text(formattedTongTien);
        },
        error: function (xhr, status, error) {
            console.error(xhr.responseText);
        }
    });

    $('#showModal').modal('show');
}


// Hàm để chuyển hướng đến trang sản phẩm
function redirectToProductPage(productId) {
    window.location.href = '/single-product/' + productId;
}


// Hàm đổi trạng thái hủy đơn hàng
function setStatusOrder(button) {
    var orderId = button.getAttribute("data-id");
    Swal.fire({
        title: "Xác nhận",
        text: "Bạn có chắc chắn muốn hủy đơn hàng này không?",
        icon: "warning",
        showCancelButton: true, // Hiển thị nút Hủy
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Đồng ý",
        cancelButtonText: "Hủy"
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: "POST",
                url: "/rest/my-order/setStatus/" + orderId,
                success: function (response) {
                    Swal.fire({
                        title: "Thành công!",
                        text: "Hủy đơn hàng thành công!",
                        icon: "success",
                    }).then(() => {
                        // Reload trang sau khi thành công
                        location.reload();
                    });
                },
                error: function () {
                    Swal.fire({
                        title: "Lỗi!",
                        text: "Đã xảy ra lỗi khi hủy đơn hàng!",
                        icon: "error",
                    });
                }
            });
        }
    });
}





