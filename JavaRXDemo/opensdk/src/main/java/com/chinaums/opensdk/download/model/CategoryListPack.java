package com.chinaums.opensdk.download.model;

import com.chinaums.opensdk.cons.DynamicResourceWorkspace;
import com.chinaums.opensdk.cons.OpenConst.DynamicDownloadConf;
import com.chinaums.opensdk.data.model.ResourceSignHistory;
import com.chinaums.opensdk.download.process.CategoryListParseProcess;
import com.chinaums.opensdk.manager.OpenConfigManager;
import com.chinaums.opensdk.manager.OpenDynamicBizHistoryManager;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CategoryListPack extends AbsListPack implements Serializable {

    private static final long serialVersionUID = -3866919390987395052L;

    /**
     * category
     */
    private Category category;

    public CategoryListPack(DynamicResourceWorkspace resourceWorkspace) {
        super(resourceWorkspace);
    }

    @Override
    protected String getPreloadResSignForUpdateSign() throws Exception {
        return OpenDynamicBizHistoryManager.getInstance().getResourcePreload()
                .getCategorySign();
    }

    @Override
    protected String getHistoryResSign() throws Exception {
        return ResourceSignHistory.getCategorySign();
    }

    @Override
    protected void updateSignHistory(String sign) throws Exception {
        ResourceSignHistory.setCategorySign(sign);
    }

    @Override
    protected String getSignUrl() {
        return OpenConfigManager
                .getProperty(OpenConfigManager.DOWNLOAD_FILE_CATEGORY_SIGN_URL);
    }

    @Override
    public String getResUrl() {
        return OpenConfigManager
                .getProperty(OpenConfigManager.DOWNLOAD_FILE_CATEGORY_URL);
    }

    @Override
    protected String getResProcessFileName() {
        return DynamicDownloadConf.CATEGORY_LIST_PROCESS_FILE_NAME;
    }

    @Override
    protected String getPrintLog(String msg) {
        return this.getClass().toString() + " [分类列表] " + msg;
    }

    @Override
    protected String getResOriginalPathSuffix() {
        return DynamicDownloadConf.CATEGORY_LIST_ORIGINAL_FOLDER;
    }

    @Override
    protected String getPreloadResPath() throws Exception {
        return DynamicDownloadConf.CATEGORY_LIST_FOLDER;
    }

    @Override
    protected String getResProcessPathSuffix() {
        return DynamicDownloadConf.CATEGORY_LIST_PROCESS_FOLDER;
    }

    @Override
    protected String getResOriginalFileName() {
        return DynamicDownloadConf.CATEGORY_LIST_ORIGINAL_FILE_NAME;
    }

    @Override
    protected boolean initPack() throws Exception {
        category = CategoryListParseProcess.getInstance().parseJsontoCategory(
                getResProcessPath() + File.separator + getResProcessFileName());
        if (category == null || category.getChildren() == null)
            return true;
        doInitCategoryIcon(category);
        return true;
    }

    private void doInitCategoryIcon(List<Category> categories) throws Exception {
        if (categories == null || categories.isEmpty())
            return;
        for (Category cate : categories) {
            doInitCategoryIcon(cate);
        }
    }

    private void doInitCategoryIcon(Category cate) throws Exception {
        if (cate == null)
            return;
        doInitCategoryIcon(cate.getChildren());
        if (cate.getCategoryIcon() == null
                || UmsStringUtils.isBlank(cate.getCategoryIcon()
                .getDownloadUrl())) {
            cate.setIconPack(new CategoryIconPack(cate.getCategoryCode(), null,
                    null));
        } else {
            cate.setIconPack(new CategoryIconPack(cate.getCategoryCode(), cate
                    .getCategoryIcon().getSign(), cate.getCategoryIcon()
                    .getDownloadUrl()));
        }
    }

    public Category getRootCategory(Set<String> categoryCodeSet)
            throws Exception {
        Category root = createNewCategory();
        if (root == null)
            return root;
        List<Category> children = getCategories(categoryCodeSet);
        root.setChildren(children);
        return root;
    }

    public List<Category> getCategories(Set<String> categoryCodeSet)
            throws Exception {
        Category cate = createNewCategory();
        List<Category> categories = new ArrayList<CategoryListPack.Category>();
        for (Iterator<Category> iter = cate.getChildren().iterator(); iter
                .hasNext(); ) {
            Category child = iter.next();
            boolean childCanRemove = removeNotContains(child.getChildren(),
                    categoryCodeSet);
            if (categoryCodeSet.contains(child.getCategoryCode())
                    || !childCanRemove) {
                categories.add(child);
            }
        }
        return categories;
    }

    private Category createNewCategory() throws Exception {
        if (category == null || category.getChildren() == null
                || category.getChildren().isEmpty())
            return null;
        Category cate = category.clone();
        return cate;
    }

    public List<CategoryIconPack> getAllCategoryIconPacks() throws Exception {
        Category root = createNewCategory();
        if (root == null) {
            return null;
        }
        List<Category> categories = new ArrayList<Category>();
        fillCategories(categories, root);
        return getCategoryIconPacks(categories);
    }

    private void fillCategories(List<Category> categories,
                                Category parentCategory) {
        if (parentCategory == null || parentCategory.getChildren() == null
                || parentCategory.getChildren().isEmpty()) {
            return;
        }
        for (Category childCategory : parentCategory.getChildren()) {
            categories.add(childCategory);
            fillCategories(categories, childCategory);
        }
    }

    public List<CategoryIconPack> getCategoryIconPacks(List<Category> categories)
            throws Exception {
        List<CategoryIconPack> categoryIconPacks = new ArrayList<CategoryIconPack>();
        if (categories == null || categories.isEmpty())
            return null;
        fillCategoryIconPacks(categories, categoryIconPacks);
        return categoryIconPacks;
    }

    public void fillCategoryIconPacks(List<Category> categories,
                                      List<CategoryIconPack> categoryIconPacks) throws Exception {
        if (categories == null || categories.isEmpty())
            return;
        for (Category cate : categories) {
            if (cate.getIconPack() == null) {
                continue;
            }
            categoryIconPacks.add(cate.getIconPack());
            fillCategoryIconPacks(cate.getChildren(), categoryIconPacks);
        }
    }

    public boolean removeNotContains(List<Category> categories,
                                     Set<String> categoryCodeSet) throws Exception {
        boolean canRemove = true;
        if (categories == null || categories.isEmpty())
            return canRemove;
        if (categoryCodeSet == null || categoryCodeSet.isEmpty())
            return canRemove;
        for (Iterator<Category> iter = categories.iterator(); iter.hasNext(); ) {
            Category category = (Category) iter.next();
            boolean childCanRemove = removeNotContains(category.getChildren(),
                    categoryCodeSet);
            if (!categoryCodeSet.contains(category.getCategoryCode())
                    && childCanRemove) {
                iter.remove();
            } else {
                canRemove = false;
            }
        }
        return canRemove;
    }

    public Category searchByCusCode(String code) {
        return searchByCode(code, true);
    }

    public Category SearchByCode(String code) {
        return searchByCode(code, false);
    }

    private Category searchByCode(String code, boolean isCusCode) {
        if (category == null || UmsStringUtils.isBlank(code)) {
            return null;
        } else if (!isCusCode && code.contains(category.getCategoryCode())) {
            return category;
        } else if (isCusCode
                && code.equalsIgnoreCase(category.getCategoryCusCode())) {
            return category;
        }
        List<Category> retCategories = new ArrayList<CategoryListPack.Category>();
        searchByCode(category.getChildren(), retCategories, code, isCusCode);
        if (retCategories.isEmpty()) {
            return null;
        } else {
            return retCategories.get(0);
        }
    }

    private void searchByCode(List<Category> categories,
                              List<Category> retCategories, String code, boolean isCusCode) {
        if (categories == null || UmsStringUtils.isBlank(code))
            return;
        if (!retCategories.isEmpty())
            return;
        for (Category cate : categories) {
            if (!isCusCode && code.contains(cate.getCategoryCode())) {
                retCategories.add(cate);
                return;
            } else if (isCusCode
                    && code.equalsIgnoreCase(cate.getCategoryCusCode())) {
                retCategories.add(cate);
                return;
            } else if (cate.getChildren() != null) {
                searchByCode(cate.getChildren(), retCategories, code, isCusCode);
            }
        }
    }

    public static class Category implements Serializable, Cloneable {

        private static final long serialVersionUID = 1L;

        /**
         * categoryCode
         */
        private String categoryCode;

        /**
         * 自定义编码
         */
        private String categoryCusCode;

        /**
         * categoryName
         */
        private String categoryName;

        /**
         * categoryPinyin
         */
        private String categoryPinyin;

        /**
         * categoryIcon
         */
        private Icon categoryIcon;

        /**
         * iconPack: 这个字段是生成的字段，报文里面没有这个字段
         */
        private CategoryIconPack iconPack;

        /**
         * children
         */
        private List<Category> children;

        @Override
        public Category clone() throws CloneNotSupportedException {
            Category category = (Category) super.clone();
            category.setCategoryIcon(this.categoryIcon != null ? this.categoryIcon
                    .clone() : category.getCategoryIcon());
            category.setIconPack(this.iconPack != null ? (CategoryIconPack) this.iconPack
                    .clone() : category.getIconPack());
            if (this.getChildren() == null || this.children.isEmpty()) {
                category.setChildren(new ArrayList<Category>());
                return category;
            }
            List<Category> copyChildren = new ArrayList<Category>(
                    children.size());
            Iterator<Category> iter = children.iterator();
            while (iter.hasNext()) {
                copyChildren.add(iter.next().clone());
            }
            category.setChildren(copyChildren);
            return category;
        }

        public String getCategoryCode() {
            return categoryCode;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getCategoryPinyin() {
            return categoryPinyin;
        }

        public Icon getCategoryIcon() {
            return categoryIcon;
        }

        public List<Category> getChildren() {
            return children;
        }

        public void setCategoryCode(String categoryCode) {
            this.categoryCode = categoryCode;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public void setCategoryPinyin(String categoryPinyin) {
            this.categoryPinyin = categoryPinyin;
        }

        public void setCategoryIcon(Icon categoryIcon) {
            this.categoryIcon = categoryIcon;
        }

        public void setChildren(List<Category> children) {
            this.children = children;
        }

        public CategoryIconPack getIconPack() {
            return iconPack;
        }

        public void setIconPack(CategoryIconPack iconPack) {
            this.iconPack = iconPack;
        }

        public String getCategoryCusCode() {
            return categoryCusCode;
        }

        public void setCategoryCusCode(String categoryCusCode) {
            this.categoryCusCode = categoryCusCode;
        }

    }

    public static class Icon implements Serializable, Cloneable {

        private static final long serialVersionUID = 4781125371812625500L;

        /**
         * downloadUrl
         */
        private String downloadUrl;

        /**
         * sign
         */
        private String sign;

        @Override
        public Icon clone() throws CloneNotSupportedException {
            return (Icon) super.clone();
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public String getSign() {
            return sign;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

    }

}