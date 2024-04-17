var listProductIdChoice = [];
var listProduct = [];
var totalPrice = 0;

// Địa chỉ
$(document).ready(function () {
    // Gọi API để lấy dữ liệu tỉnh/thành phố
    $.ajax({
        url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province',
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Token': 'a76df0d2-77a1-11ee-b1d4-92b443b7a897'
        },
        success: function (data) {
            if (data.code === 200) {
                const select = $('#provinceSelect');
                data.data.forEach(province => {
                    const option = $('<option>').val(province.ProvinceID).text(province.ProvinceName);
                    select.append(option);
                });
            } else {
                console.error("Failed to fetch province data");
            }
        },
        error: function (xhr, status, error) {
            console.error("Error fetching province data:", error);
        }
    });

    // Gọi API để lấy dữ liệu quận/huyện khi thay đổi tỉnh/thành phố
    $('#provinceSelect').change(function () {
        if ($('#provinceSelect').val() === "") {
            $('#districtSelect').empty();
            $('#districtSelect').append('<option value="">Chọn huyện</option>');

            $('#wardSelect').empty();
            $('#wardSelect').append('<option value="">Chọn xã phường</option>');
            return;
        }
        const provinceID = $(this).val();
        $.ajax({
            url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district',
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Token': 'a76df0d2-77a1-11ee-b1d4-92b443b7a897'
            },
            data: {
                province_id: provinceID
            },
            success: function (data) {
                if (data.code === 200) {
                    const select = $('#districtSelect');
                    select.empty();
                    select.append('<option value="">Chọn huyện</option>'); // Thêm dòng này để không tự động chọn
                    data.data.forEach(district => {
                        const option = $('<option>').val(district.DistrictID).text(district.DistrictName);
                        select.append(option);
                    });
                } else {
                    console.error("Failed to fetch district data");
                }
            },
            error: function (xhr, status, error) {
                console.error("Error fetching district data:", error);
            }
        });

        // Reset dropdown xã/phường khi chọn tỉnh/thành phố mới
        $('#wardSelect').empty().append('<option value="">Chọn xã phường</option>');
    });

    // Gọi API để lấy dữ liệu phường/xã khi thay đổi quận/huyện
    $('#districtSelect').change(function () {
        if ($('#districtSelect').val() === "") {
            $('#wardSelect').empty();
            $('#wardSelect').append('<option value="">Chọn xã phường</option>');
            return;
        }
        const districtID = $(this).val();
        $.ajax({
            url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=' + districtID,
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Token': 'a76df0d2-77a1-11ee-b1d4-92b443b7a897'
            },
            success: function (data) {
                if (data.code === 200) {
                    const select = $('#wardSelect');
                    select.empty();
                    select.append('<option value="">Chọn xã phường</option>'); // Thêm dòng này để không tự động chọn
                    data.data.forEach(ward => {
                        const option = $('<option>').val(ward.WardCode).text(ward.WardName);
                        select.append(option);
                    });
                } else {
                    console.error("Failed to fetch ward data");
                }
            },
            error: function (xhr, status, error) {
                console.error("Error fetching ward data:", error);
            }
        });
    });
});

$(document).ready(function () {
    var listProduct = JSON.parse(localStorage.getItem("listProductIdChoice"));

    if (listProduct) {
        listProductIdChoice = listProduct.listProductIdChoice;
    }

    console.log(listProductIdChoice);
    renderListProductChoice();
});

$(window).on("beforeunload", function () {
    // Xóa mục "listProductIdChoice" khỏi localStorage trước khi đóng trang
    localStorage.removeItem("listProductIdChoice");
});

async function renderListProductChoice() {
    for (var i = 0; i < listProductIdChoice.length; i++) {
        await $.ajax({
            type: "GET",
            url: "/cart-detail/" + listProductIdChoice[i],
            contentType: "application/json",
            success: function (response) {
                console.log("Lấy danh sách sản phẩm thành công!");

                var productDetailId = response.productDetailId;
                var productName = "" + response.productName + "<br>" + "[" + response.colorName + "-" + response.sizeName + "]";
                var quantity = response.quantity;
                var price = response.discount === 0 ? response.price : response.price / 100 * (100 - response.discount);

                var priceFomat = formatPrice(price);

                // Tạo HTML string chứa <tr> và các <td>
                var rowHTML = "<tr>" +
                    "<td class='product'>" + productName + "</td>" +
                    "<td class='quantity'>" + quantity + "</td>" +
                    "<td class='price'>" + priceFomat + "</td>" +
                    "</tr>";

                // Thêm HTML string vào tbody
                $("#listProduct").append(rowHTML);

                var productDetail = {
                    id: productDetailId,
                    name: productName,
                    quantity: quantity,
                    price: price
                }

                listProduct.push(productDetail);
                console.log(listProduct);
            },
            error: function (error) {
                console.error("Lỗi khi lấy danh sách sản phẩm:", error);
            }
        });
    }

    getTotalPrice();
}

function getTotalPrice() {
    // Lặp qua mỗi sản phẩm trong listProduct và tính tổng giá trị
    for (var i = 0; i < listProduct.length; i++) {
        totalPrice += listProduct[i].price * listProduct[i].quantity;
    }

    var formattedPrice = formatPrice(totalPrice);

    // Hiển thị tổng giá trị đã được định dạng trên giao diện người dùng
    $("#totalPriceProducts").text(formattedPrice);
    $("#totalPrice").text(formattedPrice);
    console.log(totalPrice);
}

function formatPrice(price) {
    return new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(price);
}

async function saveOrder() {
    var addressDetail = $("#address").val();
    var province = $("#provinceSelect option:selected").text();
    var district = $("#districtSelect option:selected").text();
    var ward = $("#wardSelect option:selected").text();

    var userName = $("#username").val();
    var phone = $("#phone").val();
    var address = addressDetail + ", " + ward + ", " + district + ", " + province;
    var note = $("#note").val();
    var shopping = $("input[name='paymentMethod']:checked").siblings("label").text();
    var status = 1;

    var provinceValue = $("#provinceSelect").val();
    var districtValue = $("#districtSelect").val();
    var wardValue = $("#wardSelect").val();

    if (!checkInputSaveOrder(userName, phone, addressDetail, provinceValue, districtValue, wardValue, totalPrice, shopping)) {
        return;
    }

    var dataToSend = {
        userName: userName,
        phone: phone,
        address: address,
        note: note,
        totalPrice: totalPrice,
        shopping: shopping,
        status: status
    }

    // Gửi yêu cầu AJAX
    await $.ajax({
        type: "POST",
        url: "/order/add",
        contentType: "application/json",
        data: JSON.stringify(dataToSend),
        success: function (response) {
            console.log("Lưu hóa đơn thành công!");

            var orderId = response.id;

            if (!saveOrderDetail(orderId)) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Có lỗi xảy ra khi lưu danh sách hóa đơn chi tiết!'
                });
                return;
            }

            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Đặt hàng thành công!',
                didClose: function () {

                }
            });
        },
        error: function (error) {
            console.error("Lỗi khi lưu hóa đơn:", error);

            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi đặt hàng!'
            });
        }
    });
}

async function saveOrderDetail(orderId) {
    for (var i = 0; i < listProduct.length; i++) {
        var dataToSend = {
            orderId: orderId,
            productDetailId: listProduct[i].id,
            quantity: listProduct[i].quantity,
            price: listProduct[i].price,
            status: 0
        }

        // Gửi yêu cầu AJAX
        await $.ajax({
            type: "POST",
            url: "/order-detail/add",
            contentType: "application/json",
            data: JSON.stringify(dataToSend),
            success: function (response) {
                console.log("Lưu hóa đơn chi tiết thành công!");
            },
            error: function (error) {
                console.error("Lỗi khi lưu hóa đơn:", error);
                return false;
            }
        });
    }
    return true;
}

function checkInputSaveOrder(name, phone, addressDetail, provinceValue, districtValue, wardValue, totalPrice, shopping) {
    // Regex cho tên không chứa số hoặc ký tự đặc biệt
    var nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
    // Regex cho số điện thoại
    var phoneRegex = /^0\d{9}$/;


    if (name === "" ||
        phone === "" ||
        addressDetail === "" ||
        provinceValue === "" ||
        districtValue === "" ||
        wardValue === "" ||
        totalPrice == 0 ||
        shopping === "") {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng nhập đầy đủ thông tin!'
        });
        return false;
    } else if (!nameRegex.test(name)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Tên không được chứa số hoặc ký tự đặc biệt!'
        });
        return false;
    } else if (!phoneRegex.test(phone)) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Số điện thoại không hợp lệ!'
        });
        return false;
    } else
        return true;
}