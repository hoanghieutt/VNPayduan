package com.poly.datn.sd18.repository;

import com.poly.datn.sd18.dto.response.ProductDetailCounterResponse;
import com.poly.datn.sd18.dto.response.ProductDetailResponse;
import com.poly.datn.sd18.dto.response.SizeResponse;
import com.poly.datn.sd18.entity.ProductDetail;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    @Query(value = "WITH RankedSizes AS (\n" +
            "    SELECT \n" +
            "        [id],\n" +
            "        [name],\n" +
            "        ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS sizeRank\n" +
            "    FROM \n" +
            "        [dbo].[sizes]\n" +
            ")\n" +
            "SELECT \n" +
            "    [product_details].[id],\n" +
            "    [colors].[name] AS colorName,\n" +
            "    [sizes].[name] AS sizeName,\n" +
            "    [product_details].[weight],\n" +
            "    [product_details].[quantity],\n" +
            "    [product_details].[price],\n" +
            "    [product_details].[status]\n" +
            "FROM \n" +
            "    [dbo].[product_details]\n" +
            "JOIN \n" +
            "    sizes ON product_details.size_id = sizes.id\n" +
            "JOIN \n" +
            "    colors ON product_details.color_id = colors.id\n" +
            "JOIN \n" +
            "    RankedSizes ON sizes.name = RankedSizes.name\n" +
            "WHERE \n" +
            "    [product_id] = :productId\n" +
            "ORDER BY\n" +
            "    [colors].[name],\n" +
            "    RankedSizes.sizeRank;",nativeQuery = true)
    List<ProductDetailResponse> getAllByProductId(@Param("productId") Integer productId);

    @Query(value = "SELECT sizes.id, \n" +
            "\t   sizes.name, \n" +
            "\t   sizes.shirt_length, \n" +
            "\t   sizes.shirt_width,\n" +
            "\t   sizes.status\n" +
            "FROM sizes\n" +
            "\t   CROSS JOIN colors\n" +
            "WHERE colors.id = :colorId\n" +
            "      AND NOT EXISTS (\n" +
            "          SELECT *\n" +
            "          FROM product_details\n" +
            "          WHERE color_id = :colorId\n" +
            "              AND size_id = sizes.id\n" +
            "              AND product_id = :productId)",nativeQuery = true)
    List<SizeResponse> getListSizeAddProductDetail(@Param("productId") Integer productId,@Param("colorId") Integer colorId);

    @Query("Select Count(p.id) From ProductDetail p Where p.quantity < :number")
    int countProduct(int number);

    @Query("Select p From ProductDetail p Where p.quantity < :number")
    List<ProductDetail> listProduct(int number);

    @Query("Select p From ProductDetail p")
    List<ProductDetail> getListProduct();
    @Query("select c from ProductDetail c where c.product.id =:productId")
    List<ProductDetail> getAllProductDetailByProductId(@Param("productId") Integer productId);

    @Query(value = """
            SELECT
                pd.*
            FROM
                product_details pd
                INNER JOIN
                    products p ON pd.product_id = p.id
                INNER JOIN
                    colors c ON pd.color_id = c.id
                INNER JOIN
                    sizes s ON pd.size_id = s.id
                LEFT JOIN (
                    SELECT product_id, MIN(id) AS min_image_id
                    FROM images
                    GROUP BY product_id
                ) AS min_images ON pd.product_id = min_images.product_id
                LEFT JOIN
                    images i ON min_images.min_image_id = i.id
                WHERE
                    p.id =:productId AND p.status = 0
                    AND pd.size_id =:sizeId AND s.status = 0
                    AND pd.color_id =:colorId AND c.status = 0;
        """, nativeQuery = true)
    ProductDetail findByProductIdAndColorIdAndSizeId(@Param("productId") Integer productId,
                                                     @Param("sizeId") Integer sizeId,
                                                     @Param("colorId") Integer colorId);
    @Query("select pd from ProductDetail pd where pd.id = :productDetailId")
    ProductDetail findByProductDetailId(@Param("productDetailId") Integer productDetailId);

    @Query(value = """
                SELECT pd.*
                FROM cart_details cd
                INNER JOIN product_details pd ON cd.product_detail_id = pd.id
                WHERE cd.id IN :cartDetailId
            """, nativeQuery = true)
    List<ProductDetail> findProductDetailIdByCartDetailId(@Param("cartDetailId") List<Integer> cartDetailId);
}
